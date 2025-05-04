package hsrmod.relics.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.FollowUpAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Predicate;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TheAshblazingGrandDuke extends BaseRelic implements PreBreakSubscriber {
    public static final String ID = TheAshblazingGrandDuke.class.getSimpleName();

    public TheAshblazingGrandDuke() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(AbstractCard.CardTags.STARTER_STRIKE);
            }
        })) {
            r.card.tags.add(FOLLOW_UP);
        }
    }

    @Override
    public void onUnequip() {
        super.onUnequip();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            Iterator var4 = AbstractDungeon.player.hand.group.iterator();

            AbstractCard c;
            while(var4.hasNext()) {
                c = (AbstractCard) var4.next();
                if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                    addToBot(new FollowUpAction(c));
                }
            }
        }
    }
}
