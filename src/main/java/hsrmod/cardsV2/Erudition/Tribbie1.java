package hsrmod.cardsV2.Erudition;

import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.TribbiePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Tribbie1 extends BaseCard {
    public static final String ID = Tribbie1.class.getSimpleName();
    
    public Tribbie1() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TribbiePower(magicNumber, upgraded)));
    }
}
