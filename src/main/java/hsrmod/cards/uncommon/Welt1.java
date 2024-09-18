package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.ImprisonPower;

public class Welt1 extends BaseCard {
    public static final String ID = Welt1.class.getSimpleName();
    
    public Welt1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {this.addToBot(new VFXAction(new PotionBounceEffect(p.hb.cX, p.hb.cY, m.hb.cX, this.hb.cY), 0.4F));

        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(m, new DamageInfo(p, this.damage,
                DamageInfo.DamageType.NORMAL), ElementType.Imaginary, 1, AbstractGameAction.AttackEffect.POISON,
                c -> {
                    if (!c.isDeadOrEscaped())
                        this.addToBot(new ApplyPowerAction(c, p, new ImprisonPower(c, 1), 1));
                }
        );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction));
    }
}
