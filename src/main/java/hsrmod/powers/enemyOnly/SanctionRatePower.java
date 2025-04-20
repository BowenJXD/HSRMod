package hsrmod.powers.enemyOnly;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class SanctionRatePower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(SanctionRatePower.class.getSimpleName());

    public static int stackLimit = 100;
    public static int stackCount = 33;

    CardBorderGlowManager.GlowInfo glowInfo;

    public SanctionRatePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();

        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.SKILL || abstractCard.type == AbstractCard.CardType.POWER;
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.RED;
            }

            @Override
            public String glowID() {
                return POWER_ID;
            }
        };
    }

    @Override
    public void updateDescription() {
        if (amount < stackLimit)
            this.description = String.format(DESCRIPTIONS[0], stackCount);
        else
            this.description = String.format(DESCRIPTIONS[1]);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        CardBorderGlowManager.removeGlowInfo(POWER_ID);
        CardBorderGlowManager.addGlowInfo(glowInfo);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        CardBorderGlowManager.removeGlowInfo(glowInfo);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if ((card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.POWER)
                && amount < stackLimit) {
            stackPower(stackCount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > stackLimit) {
            amount = stackLimit;
        } else if (Math.abs(stackLimit - amount) <= 1) {
            CardBorderGlowManager.removeGlowInfo(glowInfo);
            amount = stackLimit;
            AbstractMonster m = (AbstractMonster) owner;
            if (m != null) {
                if (!AbstractDungeon.actionManager.turnHasEnded) {
                    addToBot(new RollMoveAction(m));
                }
                ModHelper.addToBotAbstract(m::createIntent);
            }

            if (!AbstractDungeon.actionManager.turnHasEnded) {
                addToBot(new VFXAction(new TimeWarpTurnEndEffect()));
                addToBot(new PressEndTurnButtonAction());
            } else if (m != null) {
                AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(m));
            }

            ToughnessPower power = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
            if (power != null) {
                power.lock(this);
            }
        } else if (amount == 0) {
            ToughnessPower power = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
            if (power != null) {
                power.unlock(this);
            }
        }
        updateDescription();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        CardBorderGlowManager.removeGlowInfo(glowInfo);
    }
}
