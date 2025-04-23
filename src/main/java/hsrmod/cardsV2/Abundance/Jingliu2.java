package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

public class Jingliu2 extends BaseCard {
    public static final String ID = Jingliu2.class.getSimpleName();

    public Jingliu2() {
        super(ID);
        exhaust = true;
        cardsToPreview = new Jingliu1(true);
    }
    
    public Jingliu2(boolean asPreview) {
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
        shout(0);
        addToBot(new DiscardAction(p, p, 1, false));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractCard card = new Jingliu1();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDiscardAction(card, 1));
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }
}
