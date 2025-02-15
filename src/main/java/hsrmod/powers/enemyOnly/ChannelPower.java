package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.TheBeyond.IBanana;

public class ChannelPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(ChannelPower.class.getSimpleName());
    public String[] DESCRIPTIONS;
    public PowerStrings powerStrings;
    
    int stackLimit;
    ChannelType channelType;
    
    public ChannelPower(AbstractCreature owner, int stackLimit, ChannelType channelType) {
        this.ID = POWER_ID;
        this.amount = 0;
        this.priority = 3;
        this.stackLimit = Math.abs(stackLimit);
        this.name = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
        this.DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
        this.owner = owner;
        this.type = NeutralPowertypePatch.NEUTRAL;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        this.isTurnBased = true;
        this.canGoNegative = true;
        this.channelType = channelType;
        switchChannel(stackLimit > 0);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        switch (channelType) {
            case SURPRISE_FRIGHT:
                this.description = String.format(amount > 0 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], Math.abs(amount));
                break;
            case OFFCLASS_CLASSROOM:
                this.description = String.format(amount > 0 ? DESCRIPTIONS[2] : DESCRIPTIONS[3], Math.abs(amount));
                break;
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        alterPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }
    
    void alterPower(int alterAmount) {
        boolean isPositive = amount > 0;
        this.fontScale = 8.0F;
        amount += alterAmount;
        if (amount > stackLimit)
            amount = stackLimit;
        else if (amount < -stackLimit)
            amount = -stackLimit;
        if (isPositive && amount <= 0) {
            switchChannel(false);
        } else if (!isPositive && amount >= 0) {
            switchChannel(true);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && !owner.hasPower(TutoringPower.POWER_ID)) {
            if (amount < 0)
                stackPower(1);
            else if (amount > 0)
                reducePower(1);
        }
        return damageAmount;
    }

    public void switchChannel(boolean toPositive) {
        this.fontScale = 8.0F;
        if (toPositive) {
            addToTop(new VFXAction(new FastingEffect(owner.hb.cX, owner.hb.cY, Color.GOLD)));
            loadRegion(getRegion(channelType, true));
            amount = stackLimit;
        }
        else {
            addToTop(new VFXAction(new FastingEffect(owner.hb.cX, owner.hb.cY, Color.RED)));
            loadRegion(getRegion(channelType, false));
            amount = -stackLimit;
        }
        if (owner instanceof IBanana) {
            ((IBanana) owner).processChange(!toPositive);
        }
    }

    @Override
    protected void loadRegion(String fileName) {
        String path128 = String.format("HSRModResources/img/powers/%s128.png", fileName);
        String path48 = String.format("HSRModResources/img/powers/%s48.png", fileName);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }
    
    String getRegion(ChannelType channelType, boolean isPositive) {
        switch (channelType) {
            case SURPRISE_FRIGHT:
                return isPositive ? "Channel_SurprisePower" :"Channel_FrightPower";
            case OFFCLASS_CLASSROOM:
                return isPositive ? "Channel_OffClassPower": "Channel_ClassroomPower";
            default:
                return "Channel_SurprisePower";
        }
    }
    
    public enum ChannelType {
        SURPRISE_FRIGHT,
        OFFCLASS_CLASSROOM,
    }
}
