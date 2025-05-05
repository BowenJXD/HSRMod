package androidTestMod.cardsV2.Paths;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;

public class Abundance extends BaseCard {
    public static final String ID = Abundance.class.getSimpleName();

    public Abundance() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
    }
}
