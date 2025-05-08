package hsrmod.cardsV2.Propagation;

import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.PhenolCompoundsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PhenolCompounds extends BaseCard {
    public static final String ID = PhenolCompounds.class.getSimpleName();
    
    public PhenolCompounds() {
        super(ID);
        setBaseEnergyCost(80);
        exhaust = true;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PhenolCompoundsPower(p, magicNumber)));
    }
}
