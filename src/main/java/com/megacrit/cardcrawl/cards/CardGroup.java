package com.megacrit.cardcrawl.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import hsrmod.cards.BaseCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CardGroup {
    private static final Logger logger = LogManager.getLogger(CardGroup.class.getName());
    public ArrayList<AbstractCard> group = new ArrayList();
    public float HAND_START_X;
    public float HAND_OFFSET_X;
    private static final float HAND_HOVER_PUSH_AMT = 0.4F;
    private static final float PUSH_TAPER = 0.25F;
    private static final float TWO_CARD_PUSH_AMT = 0.2F;
    private static final float THREE_FOUR_CARD_PUSH_AMT = 0.27F;
    public static final float DRAW_PILE_X;
    public static final float DRAW_PILE_Y;
    public static final int DISCARD_PILE_X;
    public static final int DISCARD_PILE_Y = 0;
    public CardGroupType type;
    public HashMap<Integer, Integer> handPositioningMap;
    private ArrayList<AbstractCard> queued;
    private ArrayList<AbstractCard> inHand;

    public CardGroup(CardGroupType type) {
        this.HAND_START_X = (float)Settings.WIDTH * 0.36F;
        this.HAND_OFFSET_X = AbstractCard.IMG_WIDTH * 0.35F;
        this.handPositioningMap = new HashMap();
        this.queued = new ArrayList();
        this.inHand = new ArrayList();
        this.type = type;
    }

    public CardGroup(CardGroup g, CardGroupType type) {
        this.HAND_START_X = (float)Settings.WIDTH * 0.36F;
        this.HAND_OFFSET_X = AbstractCard.IMG_WIDTH * 0.35F;
        this.handPositioningMap = new HashMap();
        this.queued = new ArrayList();
        this.inHand = new ArrayList();
        this.type = type;

        for(AbstractCard c : g.group) {
            this.group.add(c.makeSameInstanceOf());
        }

    }

    public ArrayList<CardSave> getCardDeck() {
        ArrayList<CardSave> retVal = new ArrayList();

        for(AbstractCard card : this.group) {
            retVal.add(new CardSave(card.cardID, card.timesUpgraded, card.misc));
        }

        return retVal;
    }

    public ArrayList<String> getCardNames() {
        ArrayList<String> retVal = new ArrayList();

        for(AbstractCard card : this.group) {
            retVal.add(card.cardID);
        }

        return retVal;
    }

    public ArrayList<String> getCardIdsForMetrics() {
        ArrayList<String> retVal = new ArrayList();

        for(AbstractCard card : this.group) {
            retVal.add(card.getMetricID());
        }

        return retVal;
    }

    public void clear() {
        this.group.clear();
    }

    public boolean contains(AbstractCard c) {
        return this.group.contains(c);
    }

    public boolean canUseAnyCard() {
        for(AbstractCard c : this.group) {
            if (c.hasEnoughEnergy()) {
                return true;
            }
        }

        return false;
    }

    public int fullSetCheck() {
        int times = 0;
        ArrayList<String> cardIDS = new ArrayList();

        for(AbstractCard c : this.group) {
            if (c.rarity != CardRarity.BASIC) {
                cardIDS.add(c.cardID);
            }
        }

        HashMap<String, Integer> cardCount = new HashMap();

        for(String cardID : cardIDS) {
            if (cardCount.containsKey(cardID)) {
                cardCount.put(cardID, (Integer)cardCount.get(cardID) + 1);
            } else {
                cardCount.put(cardID, 1);
            }
        }

        for(Map.Entry<String, Integer> card : cardCount.entrySet()) {
            if ((Integer)card.getValue() >= 4) {
                ++times;
            }
        }

        return times;
    }

    public boolean pauperCheck() {
        for(AbstractCard c : this.group) {
            if (c.rarity == CardRarity.RARE) {
                return false;
            }
        }

        return true;
    }

    public boolean cursedCheck() {
        int count = 0;

        for(AbstractCard c : this.group) {
            if (c.type == CardType.CURSE) {
                ++count;
            }
        }

        return count >= 5;
    }

    public boolean highlanderCheck() {
        ArrayList<String> cardIDS = new ArrayList();

        for(AbstractCard c : this.group) {
            if (c.rarity != CardRarity.BASIC) {
                cardIDS.add(c.cardID);
            }
        }

        Set<String> set = new HashSet(cardIDS);
        return set.size() >= cardIDS.size();
    }

    public void applyPowers() {
        for(AbstractCard c : this.group) {
            c.applyPowers();
        }

    }

    public void removeCard(AbstractCard c) {
        this.group.remove(c);
        if (this.type == CardGroup.CardGroupType.MASTER_DECK) {
            c.onRemoveFromMasterDeck();

            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMasterDeckChange();
            }
        }

    }

    public boolean removeCard(String targetID) {
        Iterator i = this.group.iterator();

        while(i.hasNext()) {
            AbstractCard e = (AbstractCard)i.next();
            if (e.cardID.equals(targetID)) {
                i.remove();
                return true;
            }
        }

        return false;
    }

    public void addToHand(AbstractCard c) {
        c.untip();
        this.group.add(c);
    }
    
    List<AbstractCard> handCache = new ArrayList<>();

    public void refreshHandLayout() {
        if (AbstractDungeon.getCurrRoom().monsters == null || !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            if (AbstractDungeon.player.hasPower("Surrounded") && AbstractDungeon.getCurrRoom().monsters != null) {
                for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (AbstractDungeon.player.flipHorizontal) {
                        if (AbstractDungeon.player.drawX < m.drawX) {
                            m.applyPowers();
                        } else {
                            m.applyPowers();
                            m.removeSurroundedPower();
                        }
                    } else if (!AbstractDungeon.player.flipHorizontal) {
                        if (AbstractDungeon.player.drawX > m.drawX) {
                            m.applyPowers();
                        } else {
                            m.applyPowers();
                            m.removeSurroundedPower();
                        }
                    }
                }
            }

            for(AbstractOrb o : AbstractDungeon.player.orbs) {
                o.hideEvokeValues();
            }

            if (AbstractDungeon.player.hand.size() + AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() <= 3 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && AbstractDungeon.floorNum > 3) {
                UnlockTracker.unlockAchievement("PURITY");
            }

            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onRefreshHand();
            }

            float angleRange = 50.0F - (float)(10 - this.group.size()) * 5.0F;
            float incrementAngle = angleRange / (float)this.group.size();
            float sinkStart = 80.0F * Settings.scale;
            float sinkRange = 300.0F * Settings.scale;
            float incrementSink = sinkRange / (float)this.group.size() / 2.0F;
            int middle = this.group.size() / 2;

            for(int i = 0; i < this.group.size(); ++i) {
                ((AbstractCard)this.group.get(i)).setAngle(angleRange / 2.0F - incrementAngle * (float)i - incrementAngle / 2.0F);
                int t = i - middle;
                if (t >= 0) {
                    if (this.group.size() % 2 == 0) {
                        ++t;
                        t = -t;
                    } else {
                        t = -t;
                    }
                }

                if (this.group.size() % 2 == 0) {
                    ++t;
                }

                t = (int)((float)t * 1.7F);
                ((AbstractCard)this.group.get(i)).target_y = sinkStart + incrementSink * (float)t;
            }

            for(AbstractCard c : this.group) {
                c.targetDrawScale = 0.75F;
            }

            switch (this.group.size()) {
                case 0:
                    return;
                case 1:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F;
                    break;
                case 2:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.47F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.53F;
                    break;
                case 3:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    AbstractCard var10000 = (AbstractCard)this.group.get(0);
                    var10000.target_y += 20.0F * Settings.scale;
                    var10000 = (AbstractCard)this.group.get(2);
                    var10000.target_y += 20.0F * Settings.scale;
                    break;
                case 4:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.36F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.46F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.46F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.36F;
                    AbstractCard var9999 = (AbstractCard)this.group.get(1);
                    var9999.target_y -= 10.0F * Settings.scale;
                    var9999 = (AbstractCard)this.group.get(2);
                    var9999.target_y -= 10.0F * Settings.scale;
                    break;
                case 5:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.7F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.7F;
                    AbstractCard var10001 = (AbstractCard)this.group.get(0);
                    var10001.target_y += 25.0F * Settings.scale;
                    var10001 = (AbstractCard)this.group.get(2);
                    var10001.target_y -= 10.0F * Settings.scale;
                    var10001 = (AbstractCard)this.group.get(4);
                    var10001.target_y += 25.0F * Settings.scale;
                    break;
                case 6:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.1F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.3F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.43F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.43F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.3F;
                    ((AbstractCard)this.group.get(5)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.1F;
                    AbstractCard var10002 = (AbstractCard)this.group.get(0);
                    var10002.target_y += 10.0F * Settings.scale;
                    var10002 = (AbstractCard)this.group.get(5);
                    var10002.target_y += 10.0F * Settings.scale;
                    break;
                case 7:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.4F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.7F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    ((AbstractCard)this.group.get(5)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.7F;
                    ((AbstractCard)this.group.get(6)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.4F;
                    AbstractCard var10003 = (AbstractCard)this.group.get(0);
                    var10003.target_y += 25.0F * Settings.scale;
                    var10003 = (AbstractCard)this.group.get(1);
                    var10003.target_y += 18.0F * Settings.scale;
                    var10003 = (AbstractCard)this.group.get(3);
                    var10003.target_y -= 6.0F * Settings.scale;
                    var10003 = (AbstractCard)this.group.get(5);
                    var10003.target_y += 18.0F * Settings.scale;
                    var10003 = (AbstractCard)this.group.get(6);
                    var10003.target_y += 25.0F * Settings.scale;
                    break;
                case 8:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.5F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.82F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.1F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.38F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.38F;
                    ((AbstractCard)this.group.get(5)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.1F;
                    ((AbstractCard)this.group.get(6)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.77F;
                    ((AbstractCard)this.group.get(7)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.5F;
                    AbstractCard var10004 = (AbstractCard)this.group.get(1);
                    var10004.target_y += 10.0F * Settings.scale;
                    var10004 = (AbstractCard)this.group.get(6);
                    var10004.target_y += 10.0F * Settings.scale;

                    for(AbstractCard c : this.group) {
                        c.targetDrawScale = 0.7125F;
                    }
                    break;
                case 9:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.8F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.2F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.53F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.8F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.0F;
                    ((AbstractCard)this.group.get(5)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.8F;
                    ((AbstractCard)this.group.get(6)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.53F;
                    ((AbstractCard)this.group.get(7)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.2F;
                    ((AbstractCard)this.group.get(8)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.8F;
                    AbstractCard var10005 = (AbstractCard)this.group.get(1);
                    var10005.target_y += 22.0F * Settings.scale;
                    var10005 = (AbstractCard)this.group.get(2);
                    var10005.target_y += 18.0F * Settings.scale;
                    var10005 = (AbstractCard)this.group.get(3);
                    var10005.target_y += 12.0F * Settings.scale;
                    var10005 = (AbstractCard)this.group.get(5);
                    var10005.target_y += 12.0F * Settings.scale;
                    var10005 = (AbstractCard)this.group.get(6);
                    var10005.target_y += 18.0F * Settings.scale;
                    var10005 = (AbstractCard)this.group.get(7);
                    var10005.target_y += 22.0F * Settings.scale;

                    for(AbstractCard c : this.group) {
                        c.targetDrawScale = 0.67499995F;
                    }
                    break;
                case 10:
                    ((AbstractCard)this.group.get(0)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.9F;
                    ((AbstractCard)this.group.get(1)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.4F;
                    ((AbstractCard)this.group.get(2)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.8F;
                    ((AbstractCard)this.group.get(3)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.1F;
                    ((AbstractCard)this.group.get(4)).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.4F;
                    ((AbstractCard)this.group.get(5)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.4F;
                    ((AbstractCard)this.group.get(6)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.1F;
                    ((AbstractCard)this.group.get(7)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.8F;
                    ((AbstractCard)this.group.get(8)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.4F;
                    ((AbstractCard)this.group.get(9)).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.9F;
                    AbstractCard var10006 = (AbstractCard)this.group.get(1);
                    var10006.target_y += 20.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(2);
                    var10006.target_y += 17.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(3);
                    var10006.target_y += 12.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(4);
                    var10006.target_y += 5.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(5);
                    var10006.target_y += 5.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(6);
                    var10006.target_y += 12.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(7);
                    var10006.target_y += 17.0F * Settings.scale;
                    var10006 = (AbstractCard)this.group.get(8);
                    var10006.target_y += 20.0F * Settings.scale;

                    for(AbstractCard c : this.group) {
                        c.targetDrawScale = 0.63750005F;
                    }
                    break;
                default:
                    logger.info("WTF MATE, why so many cards");
            }

            AbstractCard card = AbstractDungeon.player.hoveredCard;
            if (card != null) {
                card.setAngle(0.0F);
                card.target_x = (card.current_x + card.target_x) / 2.0F;
                card.target_y = card.current_y;
            }

            for(CardQueueItem q : AbstractDungeon.actionManager.cardQueue) {
                if (q.card != null) {
                    q.card.setAngle(0.0F);
                    q.card.target_x = q.card.current_x;
                    q.card.target_y = q.card.current_y;
                }
            }

            this.glowCheck();
        }
        
        List<AbstractCard> hand = new ArrayList<>(AbstractDungeon.player.hand.group);

        // Find new cards added to hand
        for (AbstractCard card : hand) {
            if (!handCache.contains(card) && card instanceof BaseCard) {
                ((BaseCard) card).onEnterHand();
                ((BaseCard) card).inHand = true;
            }
        }

        // Find cards removed from hand
        for (AbstractCard card : handCache) {
            if (!hand.contains(card) && card instanceof BaseCard
                    && AbstractDungeon.currMapNode != null
                    && AbstractDungeon.currMapNode.room != null
                    && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                ((BaseCard) card).onLeaveHand();
                ((BaseCard) card).inHand = false;
            }
        }

        // Update handCache to the current hand
        handCache = hand;
    }

    public void glowCheck() {
        for(AbstractCard c : this.group) {
            if (c.canUse(AbstractDungeon.player, (AbstractMonster)null) && AbstractDungeon.screen != CurrentScreen.HAND_SELECT) {
                c.beginGlowing();
            } else {
                c.stopGlowing();
            }

            c.triggerOnGlowCheck();
        }

    }

    public void stopGlowing() {
        for(AbstractCard c : this.group) {
            c.stopGlowing();
        }

    }

    public void hoverCardPush(AbstractCard c) {
        if (this.group.size() > 1) {
            int cardNum = -1;

            for(int i = 0; i < this.group.size(); ++i) {
                if (c.equals(this.group.get(i))) {
                    cardNum = i;
                    break;
                }
            }

            float pushAmt = 0.4F;
            if (this.group.size() == 2) {
                pushAmt = 0.2F;
            } else if (this.group.size() == 3 || this.group.size() == 4) {
                pushAmt = 0.27F;
            }

            for(int currentSlot = cardNum + 1; currentSlot < this.group.size(); ++currentSlot) {
                AbstractCard var10000 = (AbstractCard)this.group.get(currentSlot);
                var10000.target_x += AbstractCard.IMG_WIDTH_S * pushAmt;
                pushAmt *= 0.25F;
            }

            pushAmt = 0.4F;
            if (this.group.size() == 2) {
                pushAmt = 0.2F;
            } else if (this.group.size() == 3 || this.group.size() == 4) {
                pushAmt = 0.27F;
            }

            for(int var9 = cardNum - 1; var9 > -1 && var9 < this.group.size(); --var9) {
                AbstractCard var10000 = (AbstractCard)this.group.get(var9);
                var10000.target_x -= AbstractCard.IMG_WIDTH_S * pushAmt;
                pushAmt *= 0.25F;
            }
        }

    }

    public void addToTop(AbstractCard c) {
        this.group.add(c);
    }

    public void addToBottom(AbstractCard c) {
        this.group.add(0, c);
    }

    public void addToRandomSpot(AbstractCard c) {
        if (this.group.size() == 0) {
            this.group.add(c);
        } else {
            this.group.add(AbstractDungeon.cardRandomRng.random(this.group.size() - 1), c);
        }

    }

    public AbstractCard getTopCard() {
        return (AbstractCard)this.group.get(this.group.size() - 1);
    }

    public AbstractCard getNCardFromTop(int num) {
        return (AbstractCard)this.group.get(this.group.size() - 1 - num);
    }

    public AbstractCard getBottomCard() {
        return (AbstractCard)this.group.get(0);
    }

    public AbstractCard getHoveredCard() {
        for(AbstractCard c : this.group) {
            if (c.isHoveredInHand(0.7F)) {
                boolean success = true;

                for(CardQueueItem q : AbstractDungeon.actionManager.cardQueue) {
                    if (q.card == c) {
                        success = false;
                        break;
                    }
                }

                if (success) {
                    return c;
                }
            }
        }

        return null;
    }

    public AbstractCard getRandomCard(Random rng) {
        return (AbstractCard)this.group.get(rng.random(this.group.size() - 1));
    }

    public AbstractCard getRandomCard(boolean useRng) {
        return useRng ? (AbstractCard)this.group.get(AbstractDungeon.cardRng.random(this.group.size() - 1)) : (AbstractCard)this.group.get(MathUtils.random(this.group.size() - 1));
    }

    public AbstractCard getRandomCard(boolean useRng, AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> tmp = new ArrayList();

        for(AbstractCard c : this.group) {
            if (c.rarity == rarity) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + this.type.name());
            return null;
        } else {
            Collections.sort(tmp);
            if (useRng) {
                return (AbstractCard)tmp.get(AbstractDungeon.cardRng.random(tmp.size() - 1));
            } else {
                return (AbstractCard)tmp.get(MathUtils.random(tmp.size() - 1));
            }
        }
    }

    public AbstractCard getRandomCard(Random rng, AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> tmp = new ArrayList();

        for(AbstractCard c : this.group) {
            if (c.rarity == rarity) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + this.type.name());
            return null;
        } else {
            Collections.sort(tmp);
            return (AbstractCard)tmp.get(rng.random(tmp.size() - 1));
        }
    }

    public AbstractCard getRandomCard(AbstractCard.CardType type, boolean useRng) {
        ArrayList<AbstractCard> tmp = new ArrayList();

        for(AbstractCard c : this.group) {
            if (c.type == type) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + type.name());
            return null;
        } else {
            Collections.sort(tmp);
            if (useRng) {
                return (AbstractCard)tmp.get(AbstractDungeon.cardRng.random(tmp.size() - 1));
            } else {
                return (AbstractCard)tmp.get(MathUtils.random(tmp.size() - 1));
            }
        }
    }

    public void removeTopCard() {
        this.group.remove(this.group.size() - 1);
    }

    public void shuffle() {
        Collections.shuffle(this.group, new java.util.Random(AbstractDungeon.shuffleRng.randomLong()));
    }

    public void shuffle(Random rng) {
        Collections.shuffle(this.group, new java.util.Random(rng.randomLong()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(AbstractCard c : this.group) {
            sb.append(c.cardID);
            sb.append("\n");
        }

        return sb.toString();
    }

    public void update() {
        for(AbstractCard c : this.group) {
            c.update();
        }

    }

    public void updateHoverLogic() {
        for(AbstractCard c : this.group) {
            c.updateHoverLogic();
        }

    }

    public void render(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.render(sb);
        }

    }

    public void renderShowBottled(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.render(sb);
            if (c.inBottleFlame) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Flame");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                AbstractRelic var7 = null;
            } else if (c.inBottleLightning) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Lightning");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                AbstractRelic var9 = null;
            } else if (c.inBottleTornado) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Tornado");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                Object var11 = null;
            }
        }

    }

    public void renderMasterDeck(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.render(sb);
            if (c.inBottleFlame) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Flame");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                AbstractRelic var7 = null;
            } else if (c.inBottleLightning) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Lightning");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                AbstractRelic var9 = null;
            } else if (c.inBottleTornado) {
                AbstractRelic tmp = RelicLibrary.getRelic("Bottled Tornado");
                float prevX = tmp.currentX;
                float prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                Object var11 = null;
            }
        }

    }

    public void renderExceptOneCard(SpriteBatch sb, AbstractCard card) {
        for(AbstractCard c : this.group) {
            if (!c.equals(card)) {
                c.render(sb);
            }
        }

    }

    public void renderExceptOneCardShowBottled(SpriteBatch sb, AbstractCard card) {
        for(AbstractCard c : this.group) {
            if (!c.equals(card)) {
                c.render(sb);
                if (c.inBottleFlame) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Flame");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    AbstractRelic var8 = null;
                } else if (c.inBottleLightning) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Lightning");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    AbstractRelic var10 = null;
                } else if (c.inBottleTornado) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Tornado");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    Object var12 = null;
                }
            }
        }

    }

    public void renderMasterDeckExceptOneCard(SpriteBatch sb, AbstractCard card) {
        for(AbstractCard c : this.group) {
            if (!c.equals(card)) {
                c.render(sb);
                if (c.inBottleFlame) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Flame");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    AbstractRelic var8 = null;
                } else if (c.inBottleLightning) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Lightning");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    AbstractRelic var10 = null;
                } else if (c.inBottleTornado) {
                    AbstractRelic tmp = RelicLibrary.getRelic("Bottled Tornado");
                    float prevX = tmp.currentX;
                    float prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    Object var12 = null;
                }
            }
        }

    }

    public void renderHand(SpriteBatch sb, AbstractCard exceptThis) {
        for(AbstractCard c : this.group) {
            if (c != exceptThis) {
                boolean inQueue = false;

                for(CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
                    if (i.card != null && i.card.equals(c)) {
                        this.queued.add(c);
                        inQueue = true;
                        break;
                    }
                }

                if (!inQueue) {
                    this.inHand.add(c);
                }
            }
        }

        for(AbstractCard c : this.inHand) {
            c.render(sb);
        }

        for(AbstractCard c : this.queued) {
            c.render(sb);
        }

        this.inHand.clear();
        this.queued.clear();
    }

    public void renderInLibrary(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.renderInLibrary(sb);
        }

    }

    public void renderTip(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.renderCardTip(sb);
        }

    }

    public void renderWithSelections(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.renderWithSelections(sb);
        }

    }

    public void renderDiscardPile(SpriteBatch sb) {
        for(AbstractCard c : this.group) {
            c.render(sb);
        }

    }

    public void moveToDiscardPile(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        c.darken(false);
        AbstractDungeon.getCurrRoom().souls.discard(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
    }

    public void empower(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.empower(c);
    }

    public void moveToExhaustPile(AbstractCard c) {
        for(AbstractRelic r : AbstractDungeon.player.relics) {
            r.onExhaust(c);
        }

        for(AbstractPower p : AbstractDungeon.player.powers) {
            p.onExhaust(c);
        }

        c.triggerOnExhaust();
        this.resetCardBeforeMoving(c);
        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        AbstractDungeon.player.exhaustPile.addToTop(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
    }

    public void moveToHand(AbstractCard c, CardGroup group) {
        c.unhover();
        c.lighten(true);
        c.setAngle(0.0F);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.current_x = DRAW_PILE_X;
        c.current_y = DRAW_PILE_Y;
        group.removeCard(c);
        AbstractDungeon.player.hand.addToTop(c);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }

    public void moveToHand(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.unhover();
        c.lighten(true);
        c.setAngle(0.0F);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.current_x = DRAW_PILE_X;
        c.current_y = DRAW_PILE_Y;
        AbstractDungeon.player.hand.addToTop(c);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }

    public void moveToDeck(AbstractCard c, boolean randomSpot) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.onToDeck(c, randomSpot);
    }

    public void moveToBottomOfDeck(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.onToBottomOfDeck(c);
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        if (AbstractDungeon.player.hoveredCard == c) {
            AbstractDungeon.player.releaseCard();
        }

        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        this.group.remove(c);
    }

    public boolean isEmpty() {
        return this.group.isEmpty();
    }

    private void discardAll(CardGroup discardPile) {
        for(AbstractCard c : this.group) {
            c.target_x = (float)DISCARD_PILE_X;
            c.target_y = 0.0F;
            discardPile.addToTop(c);
        }

        this.group.clear();
    }

    public void initializeDeck(CardGroup masterDeck) {
        this.clear();
        CardGroup copy = new CardGroup(masterDeck, CardGroup.CardGroupType.DRAW_PILE);
        copy.shuffle(AbstractDungeon.shuffleRng);
        ArrayList<AbstractCard> placeOnTop = new ArrayList();

        for(AbstractCard c : copy.group) {
            if (c.isInnate) {
                placeOnTop.add(c);
            } else if (!c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                c.target_x = DRAW_PILE_X;
                c.target_y = DRAW_PILE_Y;
                c.current_x = DRAW_PILE_X;
                c.current_y = DRAW_PILE_Y;
                this.addToTop(c);
            } else {
                placeOnTop.add(c);
            }
        }

        for(AbstractCard c : placeOnTop) {
            this.addToTop(c);
        }

        if (placeOnTop.size() > AbstractDungeon.player.masterHandSize) {
            AbstractDungeon.actionManager.addToTurnStart(new DrawCardAction(AbstractDungeon.player, placeOnTop.size() - AbstractDungeon.player.masterHandSize));
        }

        placeOnTop.clear();
    }

    public int size() {
        return this.group.size();
    }

    public CardGroup getUpgradableCards() {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard c : this.group) {
            if (c.canUpgrade()) {
                retVal.group.add(c);
            }
        }

        return retVal;
    }

    public Boolean hasUpgradableCards() {
        for(AbstractCard c : this.group) {
            if (c.canUpgrade()) {
                return true;
            }
        }

        return false;
    }

    public CardGroup getPurgeableCards() {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard c : this.group) {
            if (!c.cardID.equals("Necronomicurse") && !c.cardID.equals("CurseOfTheBell") && !c.cardID.equals("AscendersBane")) {
                retVal.group.add(c);
            }
        }

        return retVal;
    }

    public AbstractCard getSpecificCard(AbstractCard targetCard) {
        return this.group.contains(targetCard) ? targetCard : null;
    }

    public void triggerOnOtherCardPlayed(AbstractCard usedCard) {
        for(AbstractCard c : this.group) {
            if (c != usedCard) {
                c.triggerOnOtherCardPlayed(usedCard);
            }
        }

        for(AbstractPower p : AbstractDungeon.player.powers) {
            p.onAfterCardPlayed(usedCard);
        }

    }

    private void sortWithComparator(Comparator<AbstractCard> comp, boolean ascending) {
        if (ascending) {
            this.group.sort(comp);
        } else {
            this.group.sort(Collections.reverseOrder(comp));
        }

    }

    public void sortByRarity(boolean ascending) {
        this.sortWithComparator(new CardRarityComparator(), ascending);
    }

    public void sortByRarityPlusStatusCardType(boolean ascending) {
        this.sortWithComparator(new CardRarityComparator(), ascending);
        this.sortWithComparator(new StatusCardsLastComparator(), true);
    }

    public void sortByType(boolean ascending) {
        this.sortWithComparator(new CardTypeComparator(), ascending);
    }

    public void sortByAcquisition() {
    }

    public void sortByStatus(boolean ascending) {
        this.sortWithComparator(new CardLockednessComparator(), ascending);
    }

    public void sortAlphabetically(boolean ascending) {
        this.sortWithComparator(new CardNameComparator(), ascending);
    }

    public void sortByCost(boolean ascending) {
        this.sortWithComparator(new CardCostComparator(), ascending);
    }

    public CardGroup getSkills() {
        return this.getCardsOfType(CardType.SKILL);
    }

    public CardGroup getAttacks() {
        return this.getCardsOfType(CardType.ATTACK);
    }

    public CardGroup getPowers() {
        return this.getCardsOfType(CardType.POWER);
    }

    public CardGroup getCardsOfType(AbstractCard.CardType cardType) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard card : this.group) {
            if (card.type == cardType) {
                retVal.addToBottom(card);
            }
        }

        return retVal;
    }

    public CardGroup getGroupedByColor() {
        Map<String, CardGroup> colorGroupsMap = new LinkedHashMap();
        colorGroupsMap.put(CardColor.RED.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.GREEN.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.BLUE.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.PURPLE.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));

        for(AbstractCard.CardColor cardColor : BaseMod.getColorBundleMap().keySet()) {
            colorGroupsMap.put(cardColor.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        }

        colorGroupsMap.put(CardColor.COLORLESS.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.CURSE.name(), new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));

        for(AbstractCard card : this.group) {
            ((CardGroup)colorGroupsMap.get(card.color.name())).addToTop(card);
        }

        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(Map.Entry<String, CardGroup> e : colorGroupsMap.entrySet()) {
            CardGroup group = (CardGroup)e.getValue();
            retVal.group.addAll(group.group);
        }

        return retVal;
    }

    public AbstractCard findCardById(String id) {
        for(AbstractCard c : this.group) {
            if (c.cardID.equals(id)) {
                return c;
            }
        }

        return null;
    }

    public static CardGroup getGroupWithoutBottledCards(CardGroup group) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard c : group.group) {
            if (!c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                retVal.addToTop(c);
            }
        }

        return retVal;
    }

    static {
        DRAW_PILE_X = (float)Settings.WIDTH * 0.04F;
        DRAW_PILE_Y = 50.0F * Settings.scale;
        DISCARD_PILE_X = (int)((float)Settings.WIDTH + AbstractCard.IMG_WIDTH_S / 2.0F + 100.0F * Settings.scale);
    }

    private class CardCostComparator implements Comparator<AbstractCard> {
        private CardCostComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.cost - c2.cost;
        }
    }

    private class CardNameComparator implements Comparator<AbstractCard> {
        private CardNameComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.name.compareTo(c2.name);
        }
    }

    private class CardLockednessComparator implements Comparator<AbstractCard> {
        private CardLockednessComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            int c1Rank = 0;
            if (UnlockTracker.isCardLocked(c1.cardID)) {
                c1Rank = 2;
            } else if (!UnlockTracker.isCardSeen(c1.cardID)) {
                c1Rank = 1;
            }

            int c2Rank = 0;
            if (UnlockTracker.isCardLocked(c2.cardID)) {
                c2Rank = 2;
            } else if (!UnlockTracker.isCardSeen(c2.cardID)) {
                c2Rank = 1;
            }

            return c1Rank - c2Rank;
        }
    }

    private class CardTypeComparator implements Comparator<AbstractCard> {
        private CardTypeComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.type.compareTo(c2.type);
        }
    }

    private class StatusCardsLastComparator implements Comparator<AbstractCard> {
        private StatusCardsLastComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            if (c1.type == CardType.STATUS && c2.type != CardType.STATUS) {
                return 1;
            } else {
                return c1.type != CardType.STATUS && c2.type == CardType.STATUS ? -1 : 0;
            }
        }
    }

    private class CardRarityComparator implements Comparator<AbstractCard> {
        private CardRarityComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.rarity.compareTo(c2.rarity);
        }
    }

    public static enum CardGroupType {
        DRAW_PILE,
        MASTER_DECK,
        HAND,
        DISCARD_PILE,
        EXHAUST_PILE,
        CARD_POOL,
        UNSPECIFIED;

        private CardGroupType() {
        }
    }
}

