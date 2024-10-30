package hsrmod.relics.starter;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.modcore.CustomEnums;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

public class TrailblazeTimer extends BaseRelic /*implements ClickableRelic */ {
    public static final String ID = TrailblazeTimer.class.getSimpleName();
    AbstractCard.CardTags tag = CustomEnums.TRAILBLAZE;

    public TrailblazeTimer() {
        super(ID);
    }
    
    @Override
    public void update() {
        super.update();
        if (!isObtained) return;
        RewardEditor.getInstance().update(AbstractDungeon.getCurrRoom(), tag);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }
}
