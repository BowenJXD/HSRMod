package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ShuffleStatePower extends StatePower implements OnDrawPileShufflePower {
    public static final String POWER_ID = HSRMod.makePath(ShuffleStatePower.class.getSimpleName());

    public ShuffleStatePower(AbstractCreature owner) {
        super(POWER_ID, owner);
        this.priority = 2;
        this.amount = -1;
        
        updateDescription();
    }

    @Override
    public void onShuffle() {
        List<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.type == AbstractCard.CardType.STATUS) {
                cards.add(c);
            }
        }
        if (!cards.isEmpty()) {
            AbstractCard card = GeneralUtil.getRandomElement(cards, AbstractDungeon.cardRandomRng);
            addToBot(new MakeTempCardInDrawPileAction(card.makeCopy(), 1, true, true));
        }
    }
}
