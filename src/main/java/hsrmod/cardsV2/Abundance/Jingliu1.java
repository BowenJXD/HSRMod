package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

public class Jingliu1 extends BaseCard {
    public static final String ID = Jingliu1.class.getSimpleName();
    
    public Jingliu1() {
        super(ID);
        cardsToPreview = new Jingliu2(true);
        exhaust = true;
    }
    
    public Jingliu1(boolean asPreview) {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (cardsToPreview != null) cardsToPreview.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new LoseHPAction(p, p, magicNumber));
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        AbstractCard card = new Jingliu2();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
        addToBot(new ChangeStanceAction(new CalmStance()));
    }
}
