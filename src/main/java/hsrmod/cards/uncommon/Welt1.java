package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.ImprisonPower;

import java.util.function.Consumer;

public class Welt1 extends BaseCard {
    public static final String ID = Welt1.class.getSimpleName();
    
    public Welt1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (!ci.target.isDeadOrEscaped()) {
                            Welt1.this.addToBot(new VFXAction(new BossCrystalImpactEffect(ci.target.hb.cX, ci.target.hb.cY)));
                            Welt1.this.addToBot(new ApplyPowerAction(ci.target, p, new ImprisonPower(ci.target, 1), 1));
                        }
                    }
                }
        );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction, this));
    }
}
