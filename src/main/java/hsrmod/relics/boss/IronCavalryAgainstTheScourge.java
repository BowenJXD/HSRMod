package hsrmod.relics.boss;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import hsrmod.cards.BaseCard;
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
                    if (r.card instanceof BaseCard) {
                        BaseCard c = (BaseCard) r.card;
                        c.exhaust = true;
                        c.tr = c.baseTr += 2;
                    }
                });
    }
}
