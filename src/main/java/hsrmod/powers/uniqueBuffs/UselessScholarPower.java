package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class UselessScholarPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(UselessScholarPower.class.getSimpleName());

    public static final int ENERGY_REQUIRED = 50;
    
    int percentage;

    public UselessScholarPower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], percentage, ENERGY_REQUIRED);
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
        if (AbstractDungeon.player.hand.group.stream().anyMatch(card -> card.hasTag(FOLLOW_UP))
                && ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID) >= ENERGY_REQUIRED
                && AbstractDungeon.cardRandomRng.random(100) < percentage) {
            flash();
            addToTop(new ApplyPowerAction(owner, owner, new EnergyPower(owner, -ENERGY_REQUIRED), -ENERGY_REQUIRED));
            
            List<AbstractCard> cards = AbstractDungeon.player.hand.group.stream()
                    .filter(card -> card.hasTag(FOLLOW_UP)).collect(Collectors.toList());
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
