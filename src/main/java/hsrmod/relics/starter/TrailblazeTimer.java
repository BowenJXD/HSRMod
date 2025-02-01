package hsrmod.relics.starter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.actions.GridCardManipulator;
import hsrmod.actions.SimpleGridCardSelectBuilder;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Paths.*;
import hsrmod.events.StelleAwakeEvent;
import hsrmod.modcore.CustomEnums;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrailblazeTimer extends BaseRelic implements ClickableRelic  {
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
    public void onRightClick() {
        if (isObtained
                && (AbstractDungeon.getCurrRoom() instanceof NeowRoom || AbstractDungeon.getCurrRoom().event instanceof StelleAwakeEvent)
                && (RewardEditor.getInstance().bannedTags == null || RewardEditor.getInstance().bannedTags.isEmpty())) {
            AbstractGameAction action = new SimpleGridCardSelectBuilder(c -> true)
                    .setCardGroup(pathGroup)
                    .setAmount(pathToBan)
                    .setAnyNumber(false)
                    .setCanCancel(true)
                    .setMsg(String.format(DESCRIPTIONS[2], pathToBan))
                    .setManipulator(new GridCardManipulator() {
                                        @Override
                                        public boolean manipulate(AbstractCard card, int index, CardGroup group) {
                                            if (RewardEditor.getInstance().bannedTags == null)
                                                RewardEditor.getInstance().bannedTags = new ArrayList<>();
                                            if (!card.tags.isEmpty()) {
                                                RewardEditor.getInstance().bannedTags.add(card.tags.get(0));
                                                updateDescription(getUpdatedDescription());
                                            }
                                            return false;
                                        }
                                    }
                    );
            AbstractDungeon.effectList.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    action.update();
                    if (action.isDone) {
                        isDone = true;
                    }
                }

                @Override
                public void render(SpriteBatch spriteBatch) {
                }

                @Override
                public void dispose() {
                }
            });
        }
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }
}
