package hsrmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import hsrmod.modcore.HSRMod;

public class BleedingPower extends DoTPower {
    public static final String POWER_ID = HSRMod.makePath(BleedingPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private int damagePercentage = 1;
    
    public BleedingPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(owner, source, Amount);
        this.name = NAME;
        this.ID = POWER_ID;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        Texture tex128 = ImageMaster.loadImage(path128);
        Texture tex48 = ImageMaster.loadImage(path48);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], damagePercentage, getDamage());
    }

    @Override
    public int getDamage() {
        return Math.round(owner.maxHealth * damagePercentage / 100f);
    }
}
