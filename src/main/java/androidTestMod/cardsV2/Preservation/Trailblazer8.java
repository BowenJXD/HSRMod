package androidTestMod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.Trailblazer8Power;

public class Trailblazer8 extends BaseCard {
    public static final String ID = Trailblazer8.class.getSimpleName();

    public Trailblazer8() {
        super(ID);        
        cardsToPreview = new Quake();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        cardsToPreview.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new VFXAction(new FlameBarrierEffect(p.hb.cX, p.hb.cY)));
        addToBot(new ApplyPowerAction(p, p, new Trailblazer8Power(upgraded, magicNumber)));
    }
}
