package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.uniqueDebuffs.EpiphanyPower;

public class BlackSwan1 extends BaseCard {
    public static final String ID = BlackSwan1.class.getSimpleName();

    public BlackSwan1() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.target = CardTarget.ALL_ENEMY;
        this.isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new AOEAction((q) -> new VFXAction(new BossCrystalImpactEffect(q.hb.cX, q.hb.cY))));
            addToBot(new ElementalDamageAllAction(
                    this,
                    AbstractGameAction.AttackEffect.POISON)
                    .setCallback(ci -> {
                        addToBot(new ApplyPowerAction(ci.target, p, new WindShearPower(ci.target, p, 1), 1));
                        addToBot(new ApplyPowerAction(ci.target, p, new EpiphanyPower(ci.target, 1), 1));
                    }));
        } else {
            if (m != null)
                addToBot(new VFXAction(new BossCrystalImpactEffect(m.hb.cX, m.hb.cY)));
            addToBot(new ElementalDamageAction(
                    m,
                    new ElementalDamageInfo(this),
                    AbstractGameAction.AttackEffect.POISON,
                    ci -> {
                        addToBot(new ApplyPowerAction(ci.target, p, new WindShearPower(ci.target, p, 1), 1));
                        addToBot(new ApplyPowerAction(ci.target, p, new EpiphanyPower(ci.target, 1), 1));
                    }));
        }
    }
}
