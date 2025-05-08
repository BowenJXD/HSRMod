package hsrmod.powers.uniqueDebuffs;

import hsrmod.Hsrmod;
import hsrmod.powers.DebuffPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BurdenPower extends DebuffPower {
    public static final String POWER_ID = Hsrmod.makePath(BurdenPower.class.getSimpleName());

    public BurdenPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (m == owner || card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL) {
            remove(1);
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
