package hsrmod.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.effects.FloatingImageEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.enemyOnly.BanPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

import java.util.List;

public class SequenceTrotter extends BaseMonster implements PreBreakSubscriber {
    public static final String ID = SequenceTrotter.class.getSimpleName();
    
    TextureAtlas.AtlasRegion[] effectImages;
    float effectTimer = 0.0F;
    float effectInterval = 0.5F;
    
    public SequenceTrotter(float x, float y, int turnCount) {
        super(ID, 0F, -15.0F, 200, 200, x, y);
        this.turnCount = turnCount;
        effectImages = new TextureAtlas.AtlasRegion[12];
        String url = "HSRModResources/img/effects/SequenceTrotter/Layer %d.png";
        effectImages[0] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 1)), 0, 0, 78, 61); // 
        effectImages[1] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 2)), 0, 0, 52, 82); // 
        effectImages[2] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 3)), 0, 0, 79, 16);
        effectImages[3] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 4)), 0, 0, 71, 35); //
        effectImages[4] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 5)), 0, 0, 72, 24);
        effectImages[5] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 6)), 0, 0, 55, 72);
        effectImages[6] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 7)), 0, 0, 104, 56);
        effectImages[7] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 8)), 0, 0, 83, 83); // 
        effectImages[8] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 9)), 0, 0, 84, 32);
        effectImages[9] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 10)), 0, 0, 95, 25);
        effectImages[10] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 11)), 0, 0, 54, 24);
        effectImages[11] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(String.format(url, 12)), 0, 0, 82, 55); //
    }
    
    public SequenceTrotter(float x, float y) {
        this(x, y, 0);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        SubscriptionManager.subscribe(this);
        if (turnCount > 2) {
            ModHelper.addToBotAbstract(() -> {
                ToughnessPower power = (ToughnessPower) getPower(ToughnessPower.POWER_ID);
                if (power != null) {
                    power.lock(this);
                }
            });
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                break;
            case 1:
                ModHelper.addToBotAbstract(() -> {
                    ToughnessPower power = (ToughnessPower) getPower(ToughnessPower.POWER_ID);
                    if (power != null) {
                        power.lock(this);
                    }
                });
                break;
            case 2:
                addToBot(new EscapeAction(this));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (turnCount > 1 && !escaped && !isDead && hbAlpha > 0) {
            effectTimer += Gdx.graphics.getDeltaTime();
            if (effectTimer > effectInterval) {
                effectTimer = 0.0F;
                AbstractDungeon.effectList.add(new FloatingImageEffect(hb, effectImages));
            }
        }
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount) {
            case 0:
                setMove(MOVES[0], (byte) 0, Intent.UNKNOWN);
                break;
            case 1:
                setMove(MOVES[1], (byte) 1, Intent.UNKNOWN);
                break;
            case 2:
                setMove(MOVES[2], (byte) 2, Intent.ESCAPE);
                break;
        }
        turnCount++;
    }

    @Override
    public void die() {
        super.die();
        SubscriptionManager.unsubscribe(this);
        RewardEditor.addExtraRewardToTop(rewards -> {
            RewardItem rewardItem = new RewardItem();
            RewardEditor.setRewardCards(rewardItem, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE);
            rewards.add(rewardItem);
        });
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == this) {
            turnCount = 0;
            setMove(MOVES[0], (byte) 0, Intent.STUN);
            createIntent();
        }
    }
}
