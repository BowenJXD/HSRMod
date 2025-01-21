package hsrmod.relics.boss;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class HerosTriumphantReturn extends BaseRelic {
    public static final String ID = HerosTriumphantReturn.class.getSimpleName();
    
    public HerosTriumphantReturn() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        AbstractDungeon.player.drawPile.group.stream()
                .filter(c -> c.type == AbstractCard.CardType.POWER)
                .limit(2)
                .forEach(c -> addToBot(new NewQueueCardAction(c, true, false, true)));
    }
}
