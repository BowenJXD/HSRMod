package hsrmod.cardsV2.Propagation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueBuffs.MilitaryMeritPower;

public class Cerydra1 extends BaseCard {
    public static final String ID = hsrmod.modcore.HSRMod.makePath(Cerydra1.class.getSimpleName());

    public Cerydra1() {
        super(ID);
        setBaseEnergyCost(130);
        tags.add(CustomEnums.ENERGY_COSTING);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        } else {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot(new ApplyPowerAction(p, p, new MilitaryMeritPower(2, magicNumber), 2));
    }
}
