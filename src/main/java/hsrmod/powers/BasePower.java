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

public abstract class BasePower extends AbstractPower {
    public String[] DESCRIPTIONS;
    public boolean upgraded;
    public PowerStrings powerStrings;
    
    public BasePower(String id, AbstractCreature owner, int Amount, PowerType type, boolean upgraded){
        this.ID = id;
        this.name = CardCrawlGame.languagePack.getPowerStrings(id).NAME;
        this.DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(id).DESCRIPTIONS;
        this.owner = owner;
        this.amount = Amount;
        this.type = type;
        this.upgraded = upgraded;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
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
        String path128 = String.format("HSRModResources/img/powers/%s128.png", fileName);
        String path48 = String.format("HSRModResources/img/powers/%s48.png", fileName);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }
}
