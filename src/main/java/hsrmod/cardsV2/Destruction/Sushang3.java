package hsrmod.cardsV2.Destruction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;

public class Sushang3 extends BaseCard {
    public static final String ID = Sushang3.class.getSimpleName();

    public Sushang3() {
        super(ID);
    }

    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        upgradeTr(1);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = DataManager.getInstance().getCardData(ID, CardDataCol.Name) + "+" + this.timesUpgraded;
        initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY, 
                ci -> {
            if (ci.didBreak) {
                addToBot(new ExhaustSpecificCardAction(this, p.discardPile));
                addToBot(new ExhaustSpecificCardAction(this, p.drawPile));
                addToTop(new UpgradeSpecificCardAction(this));
                AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.uuid == uuid).findFirst().ifPresent(AbstractCard::upgrade);
            }
        }
        ));
    }
}
