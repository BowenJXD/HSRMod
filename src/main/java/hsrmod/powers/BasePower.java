package hsrmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.PathDefine;

public abstract class BasePower extends AbstractPower {
    public String[] DESCRIPTIONS;
    public boolean upgraded;
    public PowerStrings powerStrings;
    
    public BasePower(String id, AbstractCreature owner, int Amount, PowerType type, boolean upgraded){
        this.ID = id;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
        this.name = powerStrings.NAME;
        this.DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        this.owner = owner;
        this.amount = Amount;
        this.type = type;
        this.upgraded = upgraded;
        this.loadRegion(this.getClass().getSimpleName());
    }
    
    public BasePower(String id, AbstractCreature owner, int Amount, PowerType type){
        this(id, owner, Amount, type,false);
    }

    @Override
    public void updateDescription() {
        try {
            description = DESCRIPTIONS[upgraded && DESCRIPTIONS.length > 1 ? 1 : 0];
        } catch (Exception e) {
            HSRMod.logger.error("Error while updating power description", e);
        }
    }

    public void remove(int val) {
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToTop(new ReducePowerAction(this.owner, this.owner, this, val));
        }
    }

    @Override
    protected void loadRegion(String fileName) {
        try {
            String path128 = String.format(PathDefine.POWER_PATH + "%s128.png", fileName);
            String path48 = String.format(PathDefine.POWER_PATH + "%s48.png", fileName);
            this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
            this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading power region", e);
        }
    }

    protected void loadRegion(String resourcePath, String fileName) {
        try {
            String path128 = String.format(resourcePath + "%s128.png", fileName);
            String path48 = String.format(resourcePath + "%s48.png", fileName);
            this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
            this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading power region", e);
        }
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        SubscriptionManager.getInstance().triggerPrePowerTrigger(this);
    }
}
