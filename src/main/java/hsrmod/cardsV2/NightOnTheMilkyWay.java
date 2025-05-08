package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.*;
import hsrmod.cards.BaseCard;
import hsrmod.utils.RelicEventHelper;

public class NightOnTheMilkyWay extends BaseCard {
    public static final String ID = NightOnTheMilkyWay.class.getSimpleName();
    
    public NightOnTheMilkyWay() {
        super(ID);
        exhaust = true;
        cardsToPreview = new Normality();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Boolean[] cons = {false, false, false};
        for (MapEdge edge : AbstractDungeon.getCurrMapNode().getEdges()) {
            AbstractRoom room = AbstractDungeon.map.get(edge.dstY).get(edge.dstX).getRoom();
            if (room instanceof TreasureRoom || room instanceof ShopRoom) {
                cons[0] = true;
            } else if (room instanceof MonsterRoom) {
                cons[1] = true;
            } else if (room instanceof EventRoom || room instanceof RestRoom) {
                cons[2] = true;
            }
        }

        if (cons[0]) {
            RelicEventHelper.gainGold(magicNumber * 10);
            addToBot(new MakeTempCardInDrawPileAction(new Normality(), 1, false, false, true));
        }
        if (cons[1]) {
            addToBot(new DrawCardAction(p, magicNumber));
        }
        if (cons[2]) {
            addToBot(new GainEnergyAction(magicNumber));
        }
    }
}
