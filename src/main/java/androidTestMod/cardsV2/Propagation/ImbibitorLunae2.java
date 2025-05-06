package androidTestMod.cardsV2.Propagation;

import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ImbibitorLunae2 extends BaseCard {
    public static final String ID = ImbibitorLunae2.class.getSimpleName();
    
    public ImbibitorLunae2() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        addToTop(new GainEnergyAction(2));
    }
}
