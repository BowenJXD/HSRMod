package androidTestMod.relics.boss;

import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HerosTriumphantReturn extends BaseRelic {
    public static final String ID = HerosTriumphantReturn.class.getSimpleName();
    
    public HerosTriumphantReturn() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        long limit = 2;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type == AbstractCard.CardType.POWER) {
                if (limit-- == 0) break;
                if (c instanceof BaseCard) {
                    ((BaseCard) c).energyCost = 0;
                }
                HerosTriumphantReturn.this.addToBot(new FollowUpAction(c));
            }
        }
    }
}
