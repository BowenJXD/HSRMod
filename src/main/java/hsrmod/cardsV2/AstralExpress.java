package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class AstralExpress extends BaseCard {
    public static final String ID = AstralExpress.class.getSimpleName();

    public AstralExpress() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.findCards(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
                .forEach(r -> {
                    if (upgraded && r.card.canUpgrade()) addToBot(new UpgradeSpecificCardAction(r.card));
                    addToBot(new FollowUpAction(r.card));
                });
    }
}