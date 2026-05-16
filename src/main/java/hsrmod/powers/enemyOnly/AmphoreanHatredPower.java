package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ForceWaitAction;
import hsrmod.cardsV2.Trailblaze.Demiurge2;
import hsrmod.effects.TopWarningEffect;
import hsrmod.misc.RestartRunHelper;
import hsrmod.misc.VideoManager;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GAMManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import org.apache.logging.log4j.Level;
import spireTogether.network.P2P.P2PManager;

import java.util.Iterator;

public class AmphoreanHatredPower extends DebuffPower implements PreElementalDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(AmphoreanHatredPower.class.getSimpleName());

    public int HAND_THRESHOLD = 50;
    public int DRAW_THRESHOLD = 60;
    public int ENERGY_THRESHOLD = 70;
    public int CURSE_THRESHOLD = 80;
    public int HP_LOSS_THRESHOLD = 90;
    public int HP_LOSS_PERCENT = 24;
    public int FAIL_THRESHOLD = 100;

    public AmphoreanHatredPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        if (BaseMod.hasModID("spireTogether:")) {
            try {
                int playerNum = P2PManager.GetPlayerCount();
                if (playerNum > 1) {
                    HAND_THRESHOLD *= playerNum;
                    DRAW_THRESHOLD *= playerNum;
                    ENERGY_THRESHOLD *= playerNum;
                    CURSE_THRESHOLD *= playerNum;
                    HP_LOSS_THRESHOLD *= playerNum;
                    HP_LOSS_PERCENT *= playerNum;
                    FAIL_THRESHOLD *= playerNum;
                    this.amount *= playerNum;
                }
            } catch (Exception e) {
                HSRMod.logger.log(Level.WARN, "Cannot obtain playernum from spireTogether.");
            }
        }
        
        this.updateDescription();
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
        SubscriptionManager.subscribe(this);
        GAMManager.addParallelAction(POWER_ID, action -> {
            if (action instanceof DamageAction && ModHelper.check(owner)) {
                try {
                    DamageInfo info = ReflectionHacks.getPrivate(action, DamageAction.class, "info");
                    if (info.type == DamageInfo.DamageType.NORMAL) {
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
        SubscriptionManager.unsubscribe(this);
        GAMManager.removeParallelAction(POWER_ID);
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
        if (stackAmount > 0)
            applyThresholdEffects(prev, this.amount);
        else
            reverseThresholdEffects(prev, this.amount);
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        int prev = this.amount;
        super.reducePower(reduceAmount);
        amount = Math.max(this.amount, 0);
        if (reduceAmount < 0)
            applyThresholdEffects(prev, this.amount);
        else
            reverseThresholdEffects(prev, this.amount);
        updateDescription();
    }

    private void applyThresholdEffects(int prev, int curr) {
        int warningTextIndex = 0;

        if (prev < HAND_THRESHOLD && curr >= HAND_THRESHOLD) {
            BaseMod.MAX_HAND_SIZE--;
            warningTextIndex = 1;
        } else if (prev < DRAW_THRESHOLD && curr >= DRAW_THRESHOLD) {
            AbstractDungeon.player.gameHandSize--;
            warningTextIndex = 2;
        } else if (prev < ENERGY_THRESHOLD && curr >= ENERGY_THRESHOLD) {
            AbstractDungeon.player.energy.energy--;
            warningTextIndex = 3;
        } else if (prev < CURSE_THRESHOLD && curr >= CURSE_THRESHOLD) {
            addRandomCurse();
            warningTextIndex = 4;
        } else if (prev < HP_LOSS_THRESHOLD && curr >= HP_LOSS_THRESHOLD) {
            int hpLoss = AbstractDungeon.player.maxHealth * HP_LOSS_PERCENT / 100;
            AbstractDungeon.player.decreaseMaxHealth(hpLoss);
            warningTextIndex = 5;
        } else if (curr > HP_LOSS_THRESHOLD && curr < FAIL_THRESHOLD) {
            warningTextIndex = 6;
        } else if (curr >= FAIL_THRESHOLD) {
            warningTextIndex = 7;
            Iterator<AbstractGameAction> i = AbstractDungeon.actionManager.actions.iterator();

            while (i.hasNext()) {
                AbstractGameAction e = (AbstractGameAction) i.next();
                if (!(e instanceof HealAction)
                        && !(e instanceof GainBlockAction)
                        && !(e instanceof UseCardAction)
                        && !(e instanceof ElementalDamageAction.TriggerCallbackAction)
                        && e.actionType != AbstractGameAction.ActionType.DAMAGE) {
                    i.remove();
                }
            }
            ModHelper.addToTopAbstract(() -> RestartRunHelper.queuedRoomRestart = true);
            VideoManager.play("ChasingFlame", 24f, false);
            addToTop(new VFXAction(new ScreenOnFireEffect()));
        }
        if (warningTextIndex > 0) {
            CardCrawlGame.sound.playA("STANCE_ENTER_WRATH", MathUtils.random(0.0F, 0.3F));
            if (warningTextIndex == 6)
                AbstractDungeon.topLevelEffects.add(new TopWarningEffect(GeneralUtil.tryFormat(DESCRIPTIONS[warningTextIndex], curr)));
            else
                AbstractDungeon.topLevelEffects.add(new TopWarningEffect(DESCRIPTIONS[warningTextIndex]));
        }
    }

    private void reverseThresholdEffects(int prev, int curr) {
        if (prev >= HAND_THRESHOLD && curr < HAND_THRESHOLD) {
            BaseMod.MAX_HAND_SIZE++;
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

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (action.info.type == DamageInfo.DamageType.NORMAL && !(action.info.card instanceof Demiurge2)) {
                stackPower(1);
            }
        }
        return dmg;
    }
}
