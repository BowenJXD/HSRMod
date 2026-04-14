package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Remembrance.Demiurge1;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

public class FuturePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(FuturePower.class.getSimpleName());

    static final int STACK_LIMIT = 24;
    static final int TRIGGER_THRESHOLD = 12;
    
    public FuturePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], STACK_LIMIT, TRIGGER_THRESHOLD, TRIGGER_THRESHOLD);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= STACK_LIMIT) {
            amount = STACK_LIMIT;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        int stackAmount = 1;
        if (card.hasTag(CustomEnums.CHRYSOS_HEIR)) {
            stackAmount++;
        }
        addToBot(new ApplyPowerAction(owner, owner, new FuturePower(owner, stackAmount), stackAmount));
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        super.atEndOfTurnPreEndTurnCards(isPlayer);
        if (amount >= TRIGGER_THRESHOLD) {
            onSpecificTrigger();
        }
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        addToBot(new ReducePowerAction(owner, owner, this, TRIGGER_THRESHOLD));
        AbstractCard card = new Demiurge1();
        addToBot(new MakeTempCardInHandAction(card, false, true));
        addToBot(new FollowUpAction(card));
    }
}
