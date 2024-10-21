package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class ConcertForTwo extends BaseCard {
    public static final String ID = ConcertForTwo.class.getSimpleName();
    
    public ConcertForTwo() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.currentBlock > block) {
            addToBot(new LoseBlockAction(p, p, p.currentBlock - block));
        } else if (p.currentBlock < block) {
            addToBot(new GainBlockAction(p, p, block - p.currentBlock));
        }
        if (m.currentBlock > magicNumber) {
            addToBot(new LoseBlockAction(m, p, m.currentBlock - magicNumber));
        } else if (m.currentBlock < magicNumber) {
            addToBot(new GainBlockAction(m, p, magicNumber - m.currentBlock));
        }
    }
}
