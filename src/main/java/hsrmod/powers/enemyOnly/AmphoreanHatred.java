package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.screens.DeathScreen;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.utils.GAMManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class AmphoreanHatred extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath("AmphoreanHatred");

    public static final int HAND_THRESHOLD = 50;
    public static final int DRAW_THRESHOLD = 60;
    public static final int ENERGY_THRESHOLD = 70;
    public static final int CURSE_THRESHOLD = 80;
    public static final int HP_LOSS_THRESHOLD = 90;
    public static final int HP_LOSS_PERCENT = 24;
    public static final int FAIL_THRESHOLD = 100;

    public AmphoreanHatred(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    public AmphoreanHatred(AbstractCreature owner) {
        this(owner, 0);
    }

    @Override
    public void updateDescription() {
        this.description = GeneralUtil.tryFormat(DESCRIPTIONS[0],
                HAND_THRESHOLD, DRAW_THRESHOLD, ENERGY_THRESHOLD,
                CURSE_THRESHOLD, HP_LOSS_THRESHOLD, HP_LOSS_PERCENT, FAIL_THRESHOLD);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        GAMManager.addParallelAction(POWER_ID, action -> {
            if (action instanceof DamageAction) {
                try {
                    DamageInfo info = ReflectionHacks.getPrivate(action, DamageAction.class, "info");
                    if (info.type == DamageInfo.DamageType.NORMAL)  {
                        stackPower(1);
                    }
                } catch (Exception _ignored) {
                }
            }
            return false;
        });
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (amount >= HAND_THRESHOLD) {
            BaseMod.MAX_HAND_SIZE++;
        }
        if (amount >= DRAW_THRESHOLD) {
            AbstractDungeon.player.gameHandSize++;
        }
        if (amount >= ENERGY_THRESHOLD) {
            AbstractDungeon.player.energy.energy++;
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        int prev = this.amount;
        this.amount = Math.min(this.amount + stackAmount, FAIL_THRESHOLD);
        applyThresholdEffects(prev, this.amount);
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        int prev = this.amount;
        super.reducePower(reduceAmount);
        reverseThresholdEffects(prev, this.amount);
        updateDescription();
    }

    private void applyThresholdEffects(int prev, int curr) {
        if (prev < HAND_THRESHOLD && curr >= HAND_THRESHOLD) {
            BaseMod.MAX_HAND_SIZE--;
        }
        if (prev < DRAW_THRESHOLD && curr >= DRAW_THRESHOLD) {
            AbstractDungeon.player.gameHandSize--;
        }
        if (prev < ENERGY_THRESHOLD && curr >= ENERGY_THRESHOLD) {
            AbstractDungeon.player.energy.energy--;
        }
        if (prev < CURSE_THRESHOLD && curr >= CURSE_THRESHOLD) {
            addRandomCurse();
        }
        if (prev < HP_LOSS_THRESHOLD && curr >= HP_LOSS_THRESHOLD) {
            int hpLoss = AbstractDungeon.player.maxHealth * HP_LOSS_PERCENT / 100;
            AbstractDungeon.player.decreaseMaxHealth(hpLoss);
        }
        if (curr >= FAIL_THRESHOLD) {
            ModHelper.addToBotAbstract(() -> {
                AbstractDungeon.player.isDead = true;
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            });
        }
    }

    private void reverseThresholdEffects(int prev, int curr) {
        if (prev >= HAND_THRESHOLD && curr < HAND_THRESHOLD) {
            AbstractDungeon.player.masterHandSize++;
        }
        if (prev >= DRAW_THRESHOLD && curr < DRAW_THRESHOLD) {
            AbstractDungeon.player.gameHandSize++;
        }
        if (prev >= ENERGY_THRESHOLD && curr < ENERGY_THRESHOLD) {
            AbstractDungeon.player.energy.energy++;
        }
        // Crossing back below CURSE_THRESHOLD or HP_LOSS_THRESHOLD does not undo those effects
    }

    private void addRandomCurse() {
        AbstractCard curse = AbstractDungeon.getCard(AbstractCard.CardRarity.CURSE);
        if (curse != null) {
            addToBot(new MakeTempCardInHandAction(curse.makeCopy(), false));
        }
    }
}
