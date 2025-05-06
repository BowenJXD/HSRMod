package androidTestMod.cards.uncommon;

import androidTestMod.actions.AOEAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.breaks.WindShearPower;
import androidTestMod.powers.uniqueDebuffs.EpiphanyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;

import java.util.function.Consumer;
import java.util.function.Function;

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
            addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
                @Override
                public AbstractGameAction apply(AbstractMonster q) {
                    return new VFXAction(new BossCrystalImpactEffect(q.hb.cX, q.hb.cY));
                }
            }));
            addToBot(new ElementalDamageAllAction(
                    this,
                    AbstractGameAction.AttackEffect.POISON)
                    .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                        @Override
                        public void accept(ElementalDamageAction.CallbackInfo ci) {
                            BlackSwan1.this.addToBot(new ApplyPowerAction(ci.target, p, new WindShearPower(ci.target, p, 1), 1));
                            BlackSwan1.this.addToBot(new ApplyPowerAction(ci.target, p, new EpiphanyPower(ci.target, 1), 1));
                        }
                    }));
        } else {
            if (m != null)
                addToBot(new VFXAction(new BossCrystalImpactEffect(m.hb.cX, m.hb.cY)));
            addToBot(new ElementalDamageAction(
                    m,
                    new ElementalDamageInfo(this),
                    AbstractGameAction.AttackEffect.POISON,
                    new Consumer<ElementalDamageAction.CallbackInfo>() {
                        @Override
                        public void accept(ElementalDamageAction.CallbackInfo ci) {
                            BlackSwan1.this.addToBot(new ApplyPowerAction(ci.target, p, new WindShearPower(ci.target, p, 1), 1));
                            BlackSwan1.this.addToBot(new ApplyPowerAction(ci.target, p, new EpiphanyPower(ci.target, 1), 1));
                        }
                    }));
        }
    }
}
