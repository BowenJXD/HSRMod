package hsrmod.relics.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.FollowUpAction;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class MemoryCycle extends BaseRelic {
    public static final String ID = MemoryCycle.class.getSimpleName();

    public MemoryCycle() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        AtomicInteger count = new AtomicInteger();
        ModHelper.findCards(c -> c.type == AbstractCard.CardType.POWER)
                .stream().limit(magicNumber)
                .forEach(r -> {
                    r.group.removeCard(r.card);
                    count.getAndIncrement();
                });
        for (int i = count.get(); i > 0; i--) {
            AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.POWER);
            if (card != null) {
                addToBot(new FollowUpAction(card));
            }
        }
    }
}
