package androidTestMod.relics.special;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.GeneralUtil;
import androidTestMod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class MusketeerOfWildWheat extends BaseRelic {
    public static final String ID = MusketeerOfWildWheat.class.getSimpleName();

    public MusketeerOfWildWheat() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        List<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && c.canUpgrade()) {
                list.add(c);
            }
        }
        AbstractCard card = GeneralUtil.getRandomElement(
                list, 
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
