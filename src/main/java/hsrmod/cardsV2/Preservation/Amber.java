package hsrmod.cardsV2.Preservation;

import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Function;

public class Amber extends BaseCard {
    public static final String ID = Amber.class.getSimpleName();
    
    public Amber() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.currentBlock == 0)
            addToBot(new GainBlockAction(p, p, block));
        
        addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
            @Override
            public AbstractGameAction apply(AbstractMonster q) {
                if (q.currentBlock == 0)
                    return new GainBlockAction(q, magicNumber);
                return null;
            }
        }));
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (AbstractDungeon.player.currentBlock == 0)
            glowColor = GOLD_BORDER_GLOW_COLOR;
        else
            glowColor = BLUE_BORDER_GLOW_COLOR;
    }
}
