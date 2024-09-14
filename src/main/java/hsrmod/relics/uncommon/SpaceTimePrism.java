package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class SpaceTimePrism extends BaseRelic {
    public static final String ID = SpaceTimePrism.class.getSimpleName();

    public SpaceTimePrism() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atTurnStartPostDraw();
        ModHelper.addToBotAbstract(() ->{
            if (AbstractDungeon.player.hand.isEmpty()) return;
            flash();
            Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
            while (hand.hasNext()) {
                AbstractCard c = hand.next();
                if (c.canUpgrade()) {
                    addToBot(new UpgradeSpecificCardAction(c));
                }
            }
        });
    }
}
