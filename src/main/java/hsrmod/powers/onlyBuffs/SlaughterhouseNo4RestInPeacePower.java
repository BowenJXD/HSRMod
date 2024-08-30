package hsrmod.powers.onlyBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.BrokenPower;

import java.util.Collections;
import java.util.List;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class SlaughterhouseNo4RestInPeacePower extends AbstractPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(SlaughterhouseNo4RestInPeacePower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    boolean upgraded = false;

    int percentage;

    public SlaughterhouseNo4RestInPeacePower(AbstractCreature owner, int Amount, boolean upgraded, int percentage) {
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
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        boolean result = false;
        if (!(o instanceof AbstractCard)) return result;
        if (list.stream().anyMatch(mod -> mod instanceof SlaughterhouseNo4RestInPeaceModifier)) return result;
        AbstractCard card = (AbstractCard) o;
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) result = true;
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) result = true;
            }
        }
        
        return result;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new SlaughterhouseNo4RestInPeaceModifier(percentage));
    }

    public static class SlaughterhouseNo4RestInPeaceModifier extends AbstractDamageModifier{
        int percentage;
        
        public SlaughterhouseNo4RestInPeaceModifier(int percentage) {
            this.percentage = percentage;
        }
        
        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (lastDamageTaken > 0) {
                if (target.hasPower(BrokenPower.POWER_ID)) {
                    if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
                        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                }
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SlaughterhouseNo4RestInPeaceModifier(percentage);
        }
    } 
}
