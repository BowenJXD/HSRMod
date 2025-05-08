package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.breaks.ShockPower;

import java.util.function.Consumer;
import java.util.function.Function;

public class Serval1 extends BaseCard {
    public static final String ID = Serval1.class.getSimpleName();

    public Serval1() {
        super(ID);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
            @Override
            public AbstractGameAction apply(AbstractMonster q) {
                return new VFXAction(new LightningEffect(q.hb.cX, q.hb.cY));
            }
        }));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        Serval1.this.addToBot(new ApplyPowerAction(ci.target, p, new ShockPower(ci.target, p, magicNumber), magicNumber));
                    }
                })
        );
    }
}
