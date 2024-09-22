package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.HuohuoPower;
import hsrmod.powers.uniqueBuffs.RobinPower;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Robin1 extends BaseCard {
    public static final String ID = Robin1.class.getSimpleName();
    
    public Robin1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int amt = energyOnUse + magicNumber;
        addToBot(new DrawCardAction(p, amt));

        ModHelper.addToBotAbstract(() ->
        {
            int amount = amt;

            for (AbstractCard card : p.hand.group) {
                if (card.hasTag(FOLLOW_UP)
                        && card instanceof BaseCard
                        && !((BaseCard)card).followedUp) {
                    addToBot(new FollowUpAction(card));
                    if (!upgraded) amount--;
                }
                else {
                    addToBot(new FollowUpAction(card));
                    amount--;
                }
                if (amount == 0) break;
            }

            p.energy.use(EnergyPanel.totalCount);
        });
    }
}
