package hsrmod.powers.onlyBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.cards.uncommon.TopazNumby2;
import hsrmod.modcore.HSRMod;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class TopazNumbyPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(TopazNumbyPower.class.getSimpleName());
    
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    boolean upgraded = false;
    
    int stackLimit = 2;
    
    public TopazNumbyPower(AbstractCreature owner, int Amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.upgraded = upgraded;
        
        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = upgraded ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            // if player doesn't have topazNumby2, add it to hand
            if (AbstractDungeon.player.masterDeck.group.stream().noneMatch(c -> c.cardID.equals(TopazNumby2.ID))) {
                flash();
                AbstractCard card = new TopazNumby2();
                if (upgraded) card.upgrade();
                addToBot(new MakeTempCardInHandAction(card));
                this.amount = 0;
            }
            else {
                this.amount = stackLimit;
            }
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (card instanceof BaseCard && !(card instanceof TopazNumby2)){
                BaseCard c = (BaseCard) card;
                if (c.followedUp) {
                    flash();
                    stackPower(1);
                }
            }
        }
    }
}
