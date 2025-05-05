package androidTestMod.utils;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CardSelectManager implements PostUpdateSubscriber {
    private static CardSelectManager instance;
    
    private Queue<CardSelectEvent> eventQueue;

    private CardSelectManager() {
        BaseMod.subscribe(this);
        eventQueue = new LinkedList<>();
    }

    public static CardSelectManager getInstance() {
        if (instance == null) {
            instance = new CardSelectManager();
        }
        return instance;
    }
    
    public void addEvent(CardGroup group, int numsRequired, String text, boolean canCancel, UsagePreset usage) {
        addEvent(group, numsRequired, text, canCancel, usage, null);
    }

    public void addEvent(CardGroup group, int numsRequired, String text, boolean canCancel, UsagePreset usage, Consumer<List<AbstractCard>> onConfirm) {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        switch (usage) {
            case UPGRADE:
                AbstractDungeon.gridSelectScreen.open(group, numsRequired, text, true, false, canCancel, false);
                eventQueue.add(new CardSelectEvent(new Supplier<List<AbstractCard>>() {
                    @Override
                    public List<AbstractCard> get() {
                        if (AbstractDungeon.gridSelectScreen.selectedCards.size() >= numsRequired) {
                            List<AbstractCard> cards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
                            RelicEventHelper.upgradeCards(AbstractDungeon.gridSelectScreen.selectedCards.toArray(new AbstractCard[0]));
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();
                            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
                            return cards;
                        } else if (!AbstractDungeon.isScreenUp) {
                            return new ArrayList<>();
                        }
                        return null;
                    }
                }, onConfirm));
                break;
            case PURGE:
                AbstractDungeon.gridSelectScreen.open(group, numsRequired, text, false, false, canCancel, true);
                eventQueue.add(new CardSelectEvent(new Supplier<List<AbstractCard>>() {
                    @Override
                    public List<AbstractCard> get() {
                        if (AbstractDungeon.gridSelectScreen.selectedCards.size() >= numsRequired) {
                            List<AbstractCard> cards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
                            CardCrawlGame.sound.play("CARD_EXHAUST");
                            float displayCount = 0.0F;
                            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                                AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F));
                                displayCount += (float) Settings.WIDTH / 6.0F;
                                AbstractDungeon.player.masterDeck.removeCard(card);
                            }
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();
                            return cards;
                        } else if (!AbstractDungeon.isScreenUp) {
                            return new ArrayList<>();
                        }
                        return null;
                    }
                }, onConfirm));
                break;
            case TRANSFORM:
                AbstractDungeon.gridSelectScreen.open(group, numsRequired, text, false, false, canCancel, true);
                eventQueue.add(new CardSelectEvent(new Supplier<List<AbstractCard>>() {
                    @Override
                    public List<AbstractCard> get() {
                        if (AbstractDungeon.gridSelectScreen.selectedCards.size() >= numsRequired) {
                            List<AbstractCard> cards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
                            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                                card.untip();
                                card.unhover();
                                AbstractDungeon.player.masterDeck.removeCard(card);
                                AbstractDungeon.transformCard(card, true, AbstractDungeon.miscRng);
                                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && AbstractDungeon.transformedCard != null) {
                                    AbstractDungeon.transformedCard = RewardEditor.getInstance().getCardByPath(AbstractDungeon.transformedCard.rarity, new ArrayList<>());
                                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getTransformedCard(), Settings.WIDTH * MathUtils.random(0.2f, 0.8f), Settings.HEIGHT * MathUtils.random(0.2f, 0.8f), false));
                                }
                            }
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();
                            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
                            return cards;
                        } else if (!AbstractDungeon.isScreenUp) {
                            return new ArrayList<>();
                        }
                        return null;
                    }
                }, onConfirm));
                break;
        }
    }

    @Override
    public void receivePostUpdate() {
        if (!eventQueue.isEmpty()) {
            CardSelectEvent event = eventQueue.peek();
            List<AbstractCard> cards = event.onUpdate.get();
            if (cards != null) {
                eventQueue.poll();
                if (event.onConfirm != null) {
                    event.onConfirm.accept(cards);
                }
            }
        }
        if (!AbstractDungeon.isScreenUp) {
            eventQueue.clear();
        }
    }
    
    public static class CardSelectEvent {
        public Supplier<List<AbstractCard>> onUpdate;
        public Consumer<List<AbstractCard>> onConfirm;
        
        public CardSelectEvent(Supplier<List<AbstractCard>> onUpdate, Consumer<List<AbstractCard>> onConfirm) {
            this.onUpdate = onUpdate;
            this.onConfirm = onConfirm;
        }
    }
    
    public static enum UsagePreset {
        UPGRADE,
        PURGE,
        TRANSFORM,
    }
}
