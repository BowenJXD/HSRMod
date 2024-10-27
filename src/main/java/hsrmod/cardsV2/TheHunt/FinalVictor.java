package hsrmod.cardsV2.TheHunt;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class FinalVictor extends BaseCard {
    public static final String ID = FinalVictor.class.getSimpleName();
    
    public FinalVictor() {
        super(ID);
        exhaust = true;
        isEthereal = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            AbstractCard card = ModHelper.getRandomElement(p.hand.group, AbstractDungeon.cardRandomRng, c -> c.canUpgrade() && c.uuid != uuid);
            if (card != null) upgradeCard(card);
            else card = ModHelper.getRandomElement(p.hand.group, AbstractDungeon.cardRandomRng, c -> c.uuid != uuid);
            if (card != null) upgradeCard(card);
        }
        else 
            addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], c -> c.uuid != uuid, cards -> {
                if (!cards.isEmpty()) {
                    AbstractCard card = cards.get(0);
                    upgradeCard(card);
                }
            }));
    }
    
    void upgradeCard(AbstractCard card) {
        if (card == null) return;
        if (card.canUpgrade()) addToTop(new UpgradeSpecificCardAction(card));
        AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.canUpgrade() && c.uuid == card.uuid).findFirst().ifPresent(AbstractCard::upgrade);
    }
}
