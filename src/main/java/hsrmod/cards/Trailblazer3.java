package hsrmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Trailblazer3 extends BaseCard {
    public static final String ID = Trailblazer3.class.getSimpleName();

    public Trailblazer3() {
        super(ID);
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.GainBlockAction(
                        m,
                        p,
                        1
                )
        );
    }
}
