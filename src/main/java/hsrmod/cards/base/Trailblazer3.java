package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.misc.ICanChangeToTempHP;

public class Trailblazer3 extends BaseCard implements ICanChangeToTempHP {
    public static final String ID = Trailblazer3.class.getSimpleName();
    
    public boolean isTempHP = false;

    public Trailblazer3() {
        super(ID);
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void changeToTempHP() {
        this.isTempHP = true;
        tags.add(CardTags.HEALING);
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
}
