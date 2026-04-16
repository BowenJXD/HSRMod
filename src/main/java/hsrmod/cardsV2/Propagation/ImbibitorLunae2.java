package hsrmod.cardsV2.Propagation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;

public class ImbibitorLunae2 extends BaseCard {
    public static final String ID = ImbibitorLunae2.class.getSimpleName();
    
    public ImbibitorLunae2() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
        if (SignatureHelper.isUnlocked(HSRMod.makePath("ImbibitorLunae1"))) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        addToTop(new GainEnergyAction(2));
    }
}
