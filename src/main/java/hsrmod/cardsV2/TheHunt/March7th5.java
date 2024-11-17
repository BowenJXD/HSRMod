package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class March7th5 extends BaseCard {
    public static final String ID = March7th5.class.getSimpleName();
    
    public March7th5(){
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        p.hand.group.stream().filter(AbstractCard::canUpgrade).forEach(c -> addToBot(new UpgradeSpecificCardAction(c)));
    }
}
