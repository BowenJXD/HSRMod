package hsrmod.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import hsrmod.dungeons.BelobogScene;
import hsrmod.dungeons.LuofuScene;
import hsrmod.dungeons.PenaconyScene;
import hsrmod.effects.ChangeSceneEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.subscribers.PostCardMoveSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;

public abstract class TerritoryPower extends StatePower implements PostCardMoveSubscriber {
    protected CardGroup handCache;
    protected CardGroup drawCache;
    protected CardGroup discardCache;
    protected CardGroup exhaustCache;
    protected String backgroundPath;
    protected ChangeSceneEffect changeSceneEffect;

    public TerritoryPower(String id, AbstractCreature owner, int Amount, boolean upgraded) {
        super(id, owner, Amount, upgraded);
    }

    public TerritoryPower(String id, AbstractCreature owner, int Amount) {
        super(id, owner, Amount);
    }

    public TerritoryPower(String id, AbstractCreature owner, boolean upgraded) {
        super(id, owner, upgraded);
    }

    public TerritoryPower(String id, AbstractCreature owner) {
        super(id, owner);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        changeSceneEffect = new ChangeSceneEffect(ImageMaster.loadImage(backgroundPath));
        AbstractDungeon.effectList.add(changeSceneEffect);
        AbstractPlayer p = AbstractDungeon.player;
        
        handCache = new CardGroup(CardGroup.CardGroupType.HAND);
        p.hand.group.forEach(c -> handCache.addToBottom(c));
        SubscriptionManager.getInstance().extraChecks.addAll(handCache.group);
        p.hand.clear();
        
        drawCache = new CardGroup(CardGroup.CardGroupType.DRAW_PILE);
        p.drawPile.group.forEach(c -> drawCache.addToBottom(c));
        p.drawPile.clear();
        SubscriptionManager.getInstance().extraChecks.addAll(drawCache.group);
        
        discardCache = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE);
        p.discardPile.group.forEach(c -> discardCache.addToBottom(c));
        p.discardPile.clear();
        SubscriptionManager.getInstance().extraChecks.addAll(discardCache.group);
        
        exhaustCache = new CardGroup(CardGroup.CardGroupType.EXHAUST_PILE);
        p.exhaustPile.group.forEach(c -> exhaustCache.addToBottom(c));
        p.exhaustPile.clear();
        SubscriptionManager.getInstance().extraChecks.addAll(exhaustCache.group);
        
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (changeSceneEffect != null) changeSceneEffect.end();
        SubscriptionManager.unsubscribe(this);

        ModHelper.addToBotAbstract(() -> {
            AbstractPlayer p = AbstractDungeon.player;
            
            p.hand.clear();
            handCache.group.forEach(c -> p.hand.addToBottom(c));
            SubscriptionManager.getInstance().extraChecks.removeAll(handCache.group);
            
            p.drawPile.clear();
            drawCache.group.forEach(c -> p.drawPile.addToBottom(c));
            SubscriptionManager.getInstance().extraChecks.removeAll(drawCache.group);
            
            p.discardPile.clear();
            discardCache.group.forEach(c -> p.discardPile.addToBottom(c));
            SubscriptionManager.getInstance().extraChecks.removeAll(discardCache.group);
            
            p.exhaustPile.clear();
            exhaustCache.group.forEach(c -> p.exhaustPile.addToBottom(c));
            SubscriptionManager.getInstance().extraChecks.removeAll(exhaustCache.group);
        });
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
    }

    @Override
    public void postCardMove(CardGroup group, AbstractCard card, boolean in) {
        if (SubscriptionManager.checkSubscriber(this)
                && !card.hasTag(CustomEnums.TERRITORY)
                && in
        ) {
            group.removeCard(card);
        }
    }

    public static boolean isInTerritory() {
        boolean result = false;
        if (AbstractDungeon.player != null) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof TerritoryPower) {
                    result = true;
                    break;
                }
            }
        } else if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof TerritoryPower) {
                        result = true;
                        break;
                    }
                }
                if (result) break;
            }
        }
        return result;
    }
}
