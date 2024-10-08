package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import java.util.ArrayList;
import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class BountyHunterPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(BountyHunterPower.class.getSimpleName());
    
    int percentage;
    
    List<AbstractCard> cardsCache;

    public BountyHunterPower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;
        cardsCache = new ArrayList<>();
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], percentage);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        cardsCache.forEach(card -> card.returnToHand = false);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.returnToHand && cardsCache.contains(card)) {
            cardsCache.remove(card);
            card.returnToHand = false;
        }
        
        if (card.returnToHand) return;
        
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) trigger(card);
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) trigger(card);
            }
        }
    }

    void trigger(AbstractCard card){
        if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
            card.returnToHand = true;
            cardsCache.add(card);
        }
    }
}
