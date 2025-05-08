package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class SpaceTimePrism extends BaseRelic {
    public static final String ID = SpaceTimePrism.class.getSimpleName();

    public SpaceTimePrism() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atTurnStartPostDraw();
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                if (AbstractDungeon.player.hand.isEmpty()) return;
                SpaceTimePrism.this.flash();
                Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
                while (hand.hasNext()) {
                    AbstractCard c = hand.next();
                    if (c.canUpgrade()) {
                        SpaceTimePrism.this.addToBot(new UpgradeSpecificCardAction(c));
                    }
                }
            }
        });
    }
}
