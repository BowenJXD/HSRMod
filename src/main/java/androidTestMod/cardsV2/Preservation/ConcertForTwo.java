package androidTestMod.cardsV2.Preservation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import androidTestMod.cards.BaseCard;

public class ConcertForTwo extends BaseCard {
    public static final String ID = ConcertForTwo.class.getSimpleName();
    
    public ConcertForTwo() {
        super(ID);
        exhaust = true;
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.WHITE)));
        if (p.currentBlock < block) {
            addToBot(new GainBlockAction(p, p, block - p.currentBlock));
        }
        if (m.currentBlock < magicNumber) {
            addToBot(new GainBlockAction(m, p, magicNumber - m.currentBlock));
        }
    }
}
