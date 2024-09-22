/*
package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Iterator;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    // @Override
    // public void onEquip() {
    //      super.onEquip();
    //      AbstractDungeon.getCurrRoom().addCardReward(new RewardItem(HSR_PINK));
    //      AbstractDungeon.getCurrRoom().addCardToRewards();
    //      AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(HSR_PINK));
    //      AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
    //      AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    //      AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
    //      AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    // }


    public void onEquip() {
        AbstractDungeon.cardRewardScreen.open(this.getRewardCards(), (RewardItem)null, this.DESCRIPTIONS[1]);
    }

    public ArrayList<AbstractCard> getRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;

        AbstractCard card;
        for(int i = 0; i < numCards; ++i) {
            card = AbstractDungeon.getCard(AbstractDungeon.rollRarity());

            while(retVal.contains(card)) {
                card = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
            }

            retVal.add(card);
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList<>();
        Iterator var7 = retVal.iterator();

        while (var7.hasNext()) {
            card = (AbstractCard)var7.next();
            retVal2.add(card.makeCopy());
        }

        return retVal2;
    }

    public AbstractCard getCard(AbstractCard.CardRarity rarity) {
        switch (rarity) {
            case RARE:
                return AbstractDungeon.rareCardPool.getRandomCard(AbstractDungeon.miscRng);
            case UNCOMMON:
                return AbstractDungeon.uncommonCardPool.getRandomCard(AbstractDungeon.miscRng);
            case COMMON:
                return AbstractDungeon.commonCardPool.getRandomCard(AbstractDungeon.miscRng);
            default:
                return null;
        }
    }
}
*/
