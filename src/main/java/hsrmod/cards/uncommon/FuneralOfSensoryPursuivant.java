package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.FuneralOfSensoryPursuivantPower;

public class FuneralOfSensoryPursuivant extends BaseCard {
    public static final String ID = FuneralOfSensoryPursuivant.class.getSimpleName();
    
    public FuneralOfSensoryPursuivant() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FuneralOfSensoryPursuivantPower(upgraded), 1));
    }
}
