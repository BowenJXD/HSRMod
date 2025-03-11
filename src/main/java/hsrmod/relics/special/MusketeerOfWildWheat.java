package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RelicEventHelper;

import java.util.stream.Collectors;

public class MusketeerOfWildWheat extends BaseRelic {
    public static final String ID = MusketeerOfWildWheat.class.getSimpleName();

    public MusketeerOfWildWheat() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractCard card = GeneralUtil.getRandomElement(
                AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && c.canUpgrade()).collect(Collectors.toList()), 
                AbstractDungeon.miscRng);
        if (card != null) {
            RelicEventHelper.upgradeCards(card);
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            addToBot(new DrawCardAction(1));
        }
    }
}
