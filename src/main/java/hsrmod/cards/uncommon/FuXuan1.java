package hsrmod.cards.uncommon;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.MatrixOfPresciencePower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.combat.ThirdEyeEffect;

public class FuXuan1 extends BaseCard {
    public static final String ID = FuXuan1.class.getSimpleName();
    
    public FuXuan1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new ThirdEyeEffect(p.hb.cX, p.hb.cY)));
        addToBot(new ApplyPowerAction(p, p, new MatrixOfPresciencePower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));
    }
}
