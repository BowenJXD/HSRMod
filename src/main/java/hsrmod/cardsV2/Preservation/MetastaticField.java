package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.QuakePower;

public class MetastaticField extends BaseCard {
    public static final String ID = MetastaticField.class.getSimpleName();
    
    public MetastaticField() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        cardsToPreview = new Quake();
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new QuakePower(p, 1), 1));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainBlockAction(m, 4));
    }
}
