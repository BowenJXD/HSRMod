package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueDebuffs.BefogPower;

public class Lingsha1 extends BaseCard {
    public static final String ID = Lingsha1.class.getSimpleName();

    public Lingsha1() {
        super(ID);
        setBaseEnergyCost(110);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        // addToBot(new HealAction(p, p, p.hasPower(BreakEffectPower.POWER_ID) ? p.getPower(BreakEffectPower.POWER_ID).amount : 0));
        addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, upgraded ? 2 : 1)));
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.FIRE
        ));
        addToBot(new ApplyPowerAction(m, p, new BefogPower(m, magicNumber, upgraded), magicNumber));
        AbstractPower power = m.getPower(BefogPower.POWER_ID);
        if (power != null) {
            ((BefogPower) power).upgraded = true;
            power.updateDescription();
        }
    }
}
