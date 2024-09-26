package hsrmod.obsoleteCards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;

public class Amber extends BaseCard {
    public static final String ID = Amber.class.getSimpleName();
    
    public Amber() {
        super(ID);
    }

    @Override
    protected void applyPowersToBlock() {
        baseBlock = AbstractDungeon.player.maxHealth * magicNumber / 100;
        super.applyPowersToBlock();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        
        addToBot(new AOEAction((q) -> new GainBlockAction(q, upgraded ? 3 : 2)));
    }
}
