package hsrmod.relics.shop;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.Iterator;

public class AshesToAshesDustToDust extends BaseRelic implements ClickableRelic {
    public static final String ID = AshesToAshesDustToDust.class.getSimpleName();
    
    int basePrice = 50;
    int priceIncrement = 50;
    boolean cardSelected = true;
    
    public AshesToAshesDustToDust() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        updateCounter();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        ModHelper.addEffectAbstract(this::updateCounter);
    }

    void updateCounter() {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() instanceof ShopRoom) {
            setCounter(basePrice);
        }
        else {
            setCounter(-1);
        }
    }
    
    public void update() {
        super.update();
        if (!this.cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            RelicEventHelper.upgradeCards(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
        }
    }

    @Override
    public void onRightClick() {
        if (counter < 0 || counter > AbstractDungeon.player.gold) {
            return;
        }

        this.cardSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var2 = AbstractDungeon.player.masterDeck.getPurgeableCards().group.iterator();

        while(var2.hasNext()) {
            AbstractCard card = (AbstractCard)var2.next();
            tmp.addToTop(card);
        }

        if (tmp.group.isEmpty()) {
            this.cardSelected = true;
            return;
        }

        flash();
        AbstractDungeon.player.loseGold(counter);
        if (tmp.group.size() <= 1) {
            RelicEventHelper.upgradeCards(tmp.group.get(0));
        } else if (!AbstractDungeon.isScreenUp) {
            AbstractDungeon.gridSelectScreen.open(tmp, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, true, false, false, false);
        } else {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(tmp, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, true, false, false, false);
        }
        
        setCounter(counter + priceIncrement);
    }
}
