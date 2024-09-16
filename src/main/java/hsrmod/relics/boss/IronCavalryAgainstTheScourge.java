package hsrmod.relics.boss;

import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class IronCavalryAgainstTheScourge extends BaseRelic {
    public static final String ID = IronCavalryAgainstTheScourge.class.getSimpleName();

    public IronCavalryAgainstTheScourge() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        ModHelper.findCards(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
                .forEach(r -> {
                    r.card.exhaust = true;
                    r.card.magicNumber = r.card.baseMagicNumber += 3;
                    r.card.initializeDescriptionCN();
                });
    }
}
