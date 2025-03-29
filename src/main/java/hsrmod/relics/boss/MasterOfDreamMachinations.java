package hsrmod.relics.boss;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.cardsV2.AstralExpress;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardSelectManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Iterator;

public class MasterOfDreamMachinations extends BaseRelic implements ClickableRelic {
    public static final String ID = MasterOfDreamMachinations.class.getSimpleName();

    public int basePrice = 0;
    public int priceIncrement = 25;
    private boolean cardSelected = true;

    public MasterOfDreamMachinations() {
        super(ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        ModHelper.addEffectAbstract(this::updateCounter);
    }

    public void onEquip() {
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(new AstralExpress(), (float) Settings.WIDTH / 3.0F, (float) Settings.HEIGHT / 2.0F, false));
        updateCounter();
    }

    void updateCounter() {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() instanceof ShopRoom) {
            setCounter(basePrice);
            beginLongPulse();
        } else if (counter >= 0) {
            setCounter(-1);
            stopPulse();
        }
    }

    @Override
    public void onLoseGold() {
        super.onLoseGold();
        if (counter > 0 && AbstractDungeon.player.gold < counter) {
            stopPulse();
        }
    }

    @Override
    public void onGainGold() {
        super.onGainGold();
        if (counter > 0 && AbstractDungeon.player.gold >= counter) {
            beginLongPulse();
        }
    }

    public void giveCards(AbstractCard card) {
        this.cardSelected = true;
        float displayCount = 0.0F;

        card.untip();
        card.unhover();
        AbstractDungeon.player.masterDeck.removeCard(card);
        AbstractDungeon.transformCard(card, true, AbstractDungeon.miscRng);
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && AbstractDungeon.transformedCard != null) {
            AbstractDungeon.transformedCard = RewardEditor.getInstance().getCardByPath(AbstractDungeon.transformedCard.rarity, new ArrayList<>());
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getTransformedCard(), (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F, false));
        }

        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
    }

    @Override
    public void onRightClick() {
        if (counter < 0 || counter > AbstractDungeon.player.gold) {
            return;
        }

        this.cardSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var2 = AbstractDungeon.player.masterDeck.getPurgeableCards().group.iterator();

        while (var2.hasNext()) {
            AbstractCard card = (AbstractCard) var2.next();
            tmp.addToTop(card);
        }

        if (tmp.group.isEmpty()) {
            this.cardSelected = true;
            return;
        }

        flash();
        AbstractDungeon.player.loseGold(counter);
        if (tmp.group.size() <= 1) {
            this.giveCards(tmp.group.get(0));
        } else if (!AbstractDungeon.isScreenUp) {
            CardSelectManager.getInstance().addEvent(tmp, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, CardSelectManager.UsagePreset.TRANSFORM);
        } else {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            CardSelectManager.getInstance().addEvent(tmp, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, CardSelectManager.UsagePreset.TRANSFORM);
        }
        counter += priceIncrement;
    }
}
