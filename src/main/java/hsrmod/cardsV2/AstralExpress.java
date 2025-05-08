package hsrmod.cardsV2;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.function.Predicate;

public class AstralExpress extends BaseCard {
    public static final String ID = AstralExpress.class.getSimpleName();

    public AstralExpress() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(CardTags.STARTER_STRIKE);
            }
        }, true, true, true, false, false)) {
            AstralExpress.this.addToBot(new FollowUpAction(r.card));
        }
    }
}
