package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class MusketeerOfWildWheat extends BaseRelic {
    public static final String ID = MusketeerOfWildWheat.class.getSimpleName();

    public MusketeerOfWildWheat() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && c.canUpgrade()).findFirst().ifPresent(AbstractCard::upgrade);
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
