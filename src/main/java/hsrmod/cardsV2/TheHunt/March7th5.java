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
        List<AbstractCard> cards = new ArrayList<>(p.hand.group);
        
        addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : p.hand.group) {
                    if (!cards.contains(c) && c.canUpgrade()) {
                        addToTop(new UpgradeSpecificCardAction(c));
                    }
                }
                isDone = true;
            }
        }));
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        upgradeMagicNumber(1);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = DataManager.getInstance().getCardData(ID, CardDataCol.Name) + "+" + this.timesUpgraded;
        initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (c.upgraded) {
            addToBot(new UpgradeSpecificCardAction(this));
        }
    }
}
