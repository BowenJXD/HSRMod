package hsrmod.powers.onlyBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;

import java.util.ArrayList;
import java.util.List;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class BountyHunterPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(BountyHunterPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    boolean upgraded = false;

    int percentage;
    
    List<AbstractCard> cardsCache;

    public BountyHunterPower(AbstractCreature owner, int Amount, boolean upgraded, int percentage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.percentage = percentage;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
        
        cardsCache = new ArrayList<>();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.returnToHand && cardsCache.contains(card)) {
            cardsCache.remove(card);
            card.returnToHand = false;
        }
        
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) trigger(card);
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) trigger(card);
            }
        }
    }

    void trigger(AbstractCard card){
        if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
            card.returnToHand = true;
            cardsCache.add(card);
        }
    }
}
