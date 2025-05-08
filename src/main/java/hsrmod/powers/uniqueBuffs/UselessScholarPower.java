package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.Hsrmod;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class UselessScholarPower extends PowerPower {
    public static final String POWER_ID = Hsrmod.makePath(UselessScholarPower.class.getSimpleName());

    public static final int ENERGY_REQUIRED = 50;
    
    int percentage;

    public UselessScholarPower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], ENERGY_REQUIRED, percentage);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) trigger();
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) trigger();
            }
        }
    }

    void trigger(){
        boolean b = false;
        for (AbstractCard card1 : AbstractDungeon.player.hand.group) {
            if (card1.hasTag(FOLLOW_UP)) {
                b = ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID) >= ENERGY_REQUIRED && AbstractDungeon.cardRandomRng.random(100) < percentage;
                break;
            }
        }
        if (b) {
            flash();
            addToTop(new ApplyPowerAction(owner, owner, new EnergyPower(owner, -ENERGY_REQUIRED), -ENERGY_REQUIRED));

            List<AbstractCard> cards = new ArrayList<>();
            for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                if (abstractCard.hasTag(FOLLOW_UP)
                        && abstractCard instanceof BaseCard
                        && !((BaseCard) abstractCard).followedUp) {
                    cards.add(abstractCard);
                }
            }
            if (cards.isEmpty()) return;
            AbstractCard card = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
            if (card.hasTag(FOLLOW_UP)){
                addToBot(new FollowUpAction(card));
            }
            else {
                addToBot(new NewQueueCardAction(card, true, false, true));
            }
        }
    }
}
