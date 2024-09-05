package hsrmod.relics.starter;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static basemod.BaseMod.logger;
import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class WaxOfElation extends BaseRelic {
    public static final String ID = WaxOfElation.class.getSimpleName();
    
    static AbstractCard.CardTags tag = FOLLOW_UP;
    
    AbstractRoom currRoom;
    
    public WaxOfElation(){
        super(ID);
    }
/*
    @Override
    public void onEnterRoom(AbstractRoom room) {
        RewardItem cardReward = new RewardItem();
        cardReward.cards = getRewardCards();
        room.addCardReward(cardReward);
    }*/

    @Override
    public void update() {
        if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()
                && AbstractDungeon.getCurrRoom() != currRoom) {
            List<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
            for (RewardItem reward : rewards) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    reward.cards = getRewardCards();
                    currRoom = AbstractDungeon.getCurrRoom();
                    break;
                }
            }
        }
    }

    public static ArrayList<AbstractCard> getRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList();
        int numCards = 3;

        AbstractRelic r;
        for(Iterator var2 = AbstractDungeon.player.relics.iterator(); var2.hasNext(); numCards = r.changeNumberOfCardsInReward(numCards)) {
            r = (AbstractRelic)var2.next();
        }

        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }

        AbstractCard card;
        for(int i = 0; i < numCards; ++i) {
            AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
            card = null;
            switch (rarity) {
                case COMMON:
                    AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
                    if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset) {
                        AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
                    }
                case UNCOMMON:
                    break;
                case RARE:
                    AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzStartOffset;
                    break;
                default:
                    logger.info("WTF?");
            }

            boolean containsDupe = true;

            while(true) {
                while(containsDupe) {
                    containsDupe = false;
                    if (AbstractDungeon.player.hasRelic("PrismaticShard")) {
                        card = CardLibrary.getAnyColorCard(rarity);
                    } else {
                        card = getCard(rarity, tag);
                    }

                    Iterator var6 = retVal.iterator();

                    while(var6.hasNext()) {
                        AbstractCard c = (AbstractCard)var6.next();
                        if (c.cardID.equals(card.cardID)) {
                            containsDupe = true;
                            break;
                        }
                    }
                }

                if (card != null) {
                    retVal.add(card);
                }
                break;
            }
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList();
        Iterator var11 = retVal.iterator();

        while(var11.hasNext()) {
            card = (AbstractCard)var11.next();
            retVal2.add(card.makeCopy());
        }

        var11 = retVal2.iterator();

        while(true) {
            while(var11.hasNext()) {
                card = (AbstractCard)var11.next();
                if (card.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(0.1f) && card.canUpgrade()) {
                    card.upgrade();
                } else {
                    Iterator var12 = AbstractDungeon.player.relics.iterator();

                    while(var12.hasNext()) {
                        AbstractRelic relic = (AbstractRelic)var12.next();
                        relic.onPreviewObtainCard(card);
                    }
                }
            }

            return retVal2;
        }
    }
    
    public static AbstractCard getCard(AbstractCard.CardRarity rarity, AbstractCard.CardTags tag) {
        CardGroup group = null;
        switch (rarity) {
            case COMMON:
                group = AbstractDungeon.commonCardPool;
                break;
            case UNCOMMON:
                group = AbstractDungeon.uncommonCardPool;
                break;
            case RARE:
                group = AbstractDungeon.rareCardPool;
                break;
            case CURSE:
                group = AbstractDungeon.curseCardPool;
                break;
            default:
                logger.info("No rarity on getCard in Abstract Dungeon");
                return null;
        }
        
        ArrayList<AbstractCard> cards = new ArrayList<>();
        
        Iterator var3 = group.group.iterator();
        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            if (c.hasTag(tag)) {
                cards.add(c);
            }
        }
        
        if (cards.isEmpty()) {
            return group.getRandomCard(AbstractDungeon.cardRng);
        }
        
        return cards.get(AbstractDungeon.cardRng.random(cards.size() - 1));
    }
}
