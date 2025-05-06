package androidTestMod.relics.starter;

import androidTestMod.cards.BaseCard;
import androidTestMod.cardsV2.Paths.*;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;

import java.util.List;
import java.util.Objects;

public class TrailblazeTimer extends BaseRelic {
    public static final String ID = TrailblazeTimer.class.getSimpleName();
    AbstractCard.CardTags tag = CustomEnums.TRAILBLAZE;
    public AbstractCard.CardTags selectedTag;
    public CardGroup pathGroup;
    public int pathToBan = 1;

    public TrailblazeTimer() {
        super(ID);
        selectedTag = CustomEnums.TRAILBLAZE;
        pathGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        pathGroup.addToBottom(new Elation());
        pathGroup.addToBottom(new Destruction());
        pathGroup.addToBottom(new Nihility());
        pathGroup.addToBottom(new Preservation());
        pathGroup.addToBottom(new Propagation());
        pathGroup.addToBottom(new TheHunt());
        pathGroup.addToBottom(new Erudition());
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }
    
    @Override
    public void update() {
        super.update();
        if (!isObtained) return;
        RewardEditor.getInstance().tag = tag;
        if (Objects.equals(description, DESCRIPTIONS[0])
                && RewardEditor.getInstance().bannedTags != null
                && !RewardEditor.getInstance().bannedTags.isEmpty()) {
            updateDescription(getUpdatedDescription());
        }
    }

    @Override
    public String getUpdatedDescription() {
        List<AbstractCard.CardTags> tags = RewardEditor.getInstance().bannedTags;
        if (tags == null || tags.isEmpty()) {
            description = DESCRIPTIONS[0];
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < tags.size(); i++) {
                if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
                    stringBuilder.append(BaseCard.getPathStringZH(tags.get(i)));
                } else {
                    stringBuilder.append(tags.get(i));
                }
                if (i < tags.size() - 1) {
                    stringBuilder.append(" & ");
                }
            }
            description = String.format(DESCRIPTIONS[1], stringBuilder);
        }
        return description;
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }
}
