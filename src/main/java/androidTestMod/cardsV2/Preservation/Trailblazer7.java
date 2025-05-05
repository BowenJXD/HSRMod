package androidTestMod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.Trailblazer7Power;

public class Trailblazer7 extends BaseCard {
    public static final String ID = Trailblazer7.class.getSimpleName();

    public Trailblazer7() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FlameBarrierEffect(p.hb.cX, p.hb.cY)));
        addToBot(new ApplyPowerAction(p, p, new Trailblazer7Power(p, magicNumber, upgraded), magicNumber));
    }
}
