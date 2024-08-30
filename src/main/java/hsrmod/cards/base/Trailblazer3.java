package hsrmod.cards.base;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class Trailblazer3 extends BaseCard {
    public static final String ID = Trailblazer3.class.getSimpleName();

    public Trailblazer3() {
        super(ID);
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new com.megacrit.cardcrawl.actions.common.GainBlockAction(
                        p,
                        p,
                        block
                )
        );
    }
}
