package androidTestMod.cards.common;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.uniqueDebuffs.BurdenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;

public class Hanya1 extends BaseCard {
    public static final String ID = Hanya1.class.getSimpleName();

    public Hanya1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new FlyingDaggerEffect(m.hb.cX, m.hb.cY, 0, false)));
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        ));
        addToBot(new ApplyPowerAction(m, p, new BurdenPower(m, magicNumber), magicNumber));
    }
}
