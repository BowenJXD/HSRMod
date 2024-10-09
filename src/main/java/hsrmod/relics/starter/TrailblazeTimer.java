package hsrmod.relics.starter;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.relics.BaseRelic;

public class TrailblazeTimer extends BaseRelic /*implements ClickableRelic */{
    public static final String ID = TrailblazeTimer.class.getSimpleName();
    private boolean cardSelected = true;
    public boolean canTransform = false;

    public TrailblazeTimer() {
        super(ID);
    }

/*    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (room instanceof ShopRoom) {
            this.canTransform = true;
            this.beginLongPulse();
            this.counter = 25;
        }
        else {
            this.stopPulse();
            this.counter = -1;
        }
    }*/

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }
/*
    @Override
    public void onRightClick() {
        if (canTransform 
                && AbstractDungeon.isPlayerInDungeon() 
                && AbstractDungeon.player != null 
                && AbstractDungeon.player.gold >= counter 
                && !AbstractDungeon.player.masterDeck.getPurgeableCards().isEmpty()) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(),
                    1, "变化一张牌", false, true, false, false);
            this.counter += 25;
        }
    }*/
}
