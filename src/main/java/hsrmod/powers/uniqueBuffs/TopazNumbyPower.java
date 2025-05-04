package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.cards.uncommon.TopazNumby2;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import java.util.function.Predicate;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TopazNumbyPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(TopazNumbyPower.class.getSimpleName());
    
    int stackLimit = 2;
    
    public TopazNumbyPower(int Amount, boolean upgraded) {
        super(POWER_ID, Amount, upgraded);
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            // if player doesn't have topazNumby2, add it to hand
            boolean b = true;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.cardID.equals(TopazNumby2.ID)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                flash();
                AbstractCard card = new TopazNumby2();
                if (upgraded) card.upgrade();
                addToBot(new MakeTempCardInHandAction(card));
                this.amount = 0;
            }
            else {
                this.amount = stackLimit;
            }
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (card instanceof BaseCard && !(card instanceof TopazNumby2)){
                BaseCard c = (BaseCard) card;
                if (c.followedUp) {
                    flash();
                    stackPower(1);
                }
            }
        }
    }
}
