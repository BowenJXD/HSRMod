package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cardsV2.Remembrance.Cyrene4;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.PowerPower;
import hsrmod.powers.TerritoryPower;
import hsrmod.utils.GeneralUtil;

public class RecollectionPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(RecollectionPower.class.getSimpleName());

    static final int TRIGGER_THRESHOLD = 24;
    
    public RecollectionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], TRIGGER_THRESHOLD);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        int stackAmount = 1;
        if (card.hasTag(CustomEnums.CHRYSOS_HEIR)) {
            stackAmount++;
        }
        addToBot(new ApplyPowerAction(owner, owner, new RecollectionPower(owner, stackAmount), stackAmount));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= TRIGGER_THRESHOLD) {
            amount = TRIGGER_THRESHOLD;
            if (!TerritoryPower.isInTerritory()) {
                onSpecificTrigger();
            }
        }
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        addToBot(new MakeTempCardInHandAction(new Cyrene4()));
    }
}
