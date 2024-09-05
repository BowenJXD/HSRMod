package hsrmod.powers.uniqueDebuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.uncommon.DrRatio3;
import hsrmod.modcore.HSRMod;

import java.util.Iterator;

public class WisemansFollyPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(WisemansFollyPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WisemansFollyPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;

        this.amount = Amount;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.NORMAL 
                || info.owner != AbstractDungeon.player) {
            return damageAmount;
        }
        
        Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
        
        int debuffNum = 0;
        int chance = 40;
        for (AbstractPower power : owner.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffNum++;
                chance += 20;
            }
        }

        while (hand.hasNext()) {
            AbstractCard card = hand.next();
            if (card instanceof DrRatio3) {
                DrRatio3 c = (DrRatio3) card;
                if (c.followedUp) continue;
                c.debuffNum = debuffNum;

                if (AbstractDungeon.cardRandomRng.random(100) < chance) {
                    c.followedUp = true;
                    addToBot(new FollowUpAction(card, owner));
                }
            }
        }
        
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        boolean hasDrRatio3 = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof DrRatio3) {
                hasDrRatio3 = true;
                break;
            }
        }
        
        if (!hasDrRatio3) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onRemove() {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.hand.group.iterator();
        
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card instanceof DrRatio3) {
                addToBot(new DiscardSpecificCardAction(card));
            }
        }
    }
}
