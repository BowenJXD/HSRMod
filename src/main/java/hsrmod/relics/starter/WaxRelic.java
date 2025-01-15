package hsrmod.relics.starter;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.actions.GridCardManipulator;
import hsrmod.actions.SimpleGridCardSelectBuilder;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Paths.*;
import hsrmod.characters.StellaCharacter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.StelleAwakeWithNeow;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.DataManager;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class WaxRelic extends BaseRelic implements ClickableRelic/*, CustomSavable<String>*/ {
    public static int defaultWeight = 1;
    protected int weight;
    public AbstractCard.CardTags selectedTag;
    public CardGroup pathGroup;
    public int pathToBan = 2;

    public WaxRelic(String id, AbstractCard.CardTags tag, int weight) {
        super(id);
        this.selectedTag = tag;
        this.weight = weight;
        pathGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (tag != CustomEnums.ELATION) pathGroup.addToBottom(new Elation());
        if (tag != CustomEnums.DESTRUCTION) pathGroup.addToBottom(new Destruction());
        if (tag != CustomEnums.NIHILITY) pathGroup.addToBottom(new Nihility());
        if (tag != CustomEnums.PRESERVATION) pathGroup.addToBottom(new Preservation());
        if (tag != CustomEnums.PROPAGATION) pathGroup.addToBottom(new Propagation());
        if (tag != CustomEnums.THE_HUNT) pathGroup.addToBottom(new TheHunt());
        if (tag != CustomEnums.ERUDITION) pathGroup.addToBottom(new Erudition());
    }

    public WaxRelic(String id, AbstractCard.CardTags tag) {
        this(id, tag, defaultWeight);
    }

    @Override
    public void update() {
        super.update();
        if (!isObtained) return;
        RewardEditor.getInstance().update(AbstractDungeon.getCurrRoom(), selectedTag);
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
                && (AbstractDungeon.getCurrRoom() instanceof NeowRoom || AbstractDungeon.getCurrRoom() instanceof StelleAwakeWithNeow.PathSelectEventRoom)
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

    public static AbstractCard.CardTags getSelectedPathTag(List<AbstractRelic> relics) {
        for (AbstractRelic r : relics) {
            if (r instanceof WaxRelic) {
                return ((WaxRelic) r).selectedTag;
            }
        }
        return null;
    }

    public static WaxRelic getWaxRelic(List<AbstractRelic> relics, AbstractCard.CardTags tag) {
        for (AbstractRelic r : relics) {
            if (r instanceof WaxRelic && ((WaxRelic) r).selectedTag == tag) {
                return (WaxRelic) r.makeCopy();
            }
        }
        return null;
    }/*

    @Override
    public void onLoad(String s) {
        descriptionAddOns = s;
        if (!s.isEmpty()) {
            description = String.format(DESCRIPTIONS[1], descriptionAddOns);
            updateDescription();
        }
    }

    @Override
    public String onSave() {
        return descriptionAddOns;
    }*/
}
