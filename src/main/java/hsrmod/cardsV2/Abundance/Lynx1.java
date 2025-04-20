package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import hsrmod.actions.CleanAction;
import hsrmod.cards.BaseCard;

public class Lynx1 extends BaseCard {
    public static final String ID = Lynx1.class.getSimpleName();

    public Lynx1() {
        super(ID);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AddTemporaryHPAction(p, p, block));
        addToBot(new CleanAction(p, 1, false));
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }
}
