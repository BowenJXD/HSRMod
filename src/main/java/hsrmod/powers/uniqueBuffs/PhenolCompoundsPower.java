package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.SporePower;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class PhenolCompoundsPower extends BuffPower {
    public static final String POWER_ID = Hsrmod.makePath(PhenolCompoundsPower.class.getSimpleName());
    
    public PhenolCompoundsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        int e = card.cost == -1 ? card.energyOnUse : card.costForTurn;
        addToBot(new GainEnergyAction(e));
        remove(1);
        AbstractCreature target = action.target == null ? ModHelper.betterGetRandomMonster() : action.target;
        if (target != null)
            addToBot(new ApplyPowerAction(target, target, new SporePower(target, amount), amount));
    }
}
