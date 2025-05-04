package hsrmod.relics.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.function.Predicate;

public class IronCavalryAgainstTheScourge extends BaseRelic {
    public static final String ID = IronCavalryAgainstTheScourge.class.getSimpleName();

    public IronCavalryAgainstTheScourge() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                c.exhaust = true;
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(AbstractCard.CardTags.STARTER_STRIKE);
            }
        })) {
            if (r.card instanceof BaseCard) {
                BaseCard c = (BaseCard) r.card;
                c.exhaust = true;
                c.tr = c.baseTr += 3;
            }
        }
    }
}
