package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Trailblaze.Demiurge2;
import hsrmod.cardsV2.Trailblaze.Demiurge3;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class AmphoreanLovePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(AmphoreanLovePower.class.getSimpleName());
    public static final int TRIGGER_THRESHOLD = 12;
    public static final int END_TRIGGER_THRESHOLD = 4;
    
    public AmphoreanLovePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = GeneralUtil.tryFormat(DESCRIPTIONS[upgraded ? 1 : 0], TRIGGER_THRESHOLD, END_TRIGGER_THRESHOLD);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        stackPower(1);
        if (upgraded) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != owner && m.hasPower(AmphoreanHatredPower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(m, owner, new AmphoreanHatredPower(m, -1)));
                }
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= TRIGGER_THRESHOLD) {
            onSpecificTrigger();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        ModHelper.addToBotAbstract(() -> {
            if (isPlayer && amount >= END_TRIGGER_THRESHOLD) {
                onSpecificTrigger();
            }
        });
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        flash();
        amount = 0;
        updateDescription();
        AbstractCard demiurge = new Demiurge2();
        if (upgraded) {
            demiurge.upgrade();
            addToBot(new FollowUpAction(demiurge));
            addToBot(new FollowUpAction(new Demiurge3()));
        } else {
            addToBot(new FollowUpAction(demiurge));
        }
    }

    public void upgrade() {
        upgraded = true;
        updateDescription();
    }
}
