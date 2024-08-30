package hsrmod.powers.breaks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.DoTPower;

public class WindShearPower extends DoTPower {
    public static final String POWER_ID = HSRMod.makePath(WindShearPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int damage = 3;
    
    private int removeLimit = 5;

    public WindShearPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(owner, source, Amount);
        this.name = NAME;
        this.ID = POWER_ID;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], removeLimit, damage, getDamage());
    }

    @Override
    public int getDamage() {
        return damage * Math.min(amount, removeLimit);
    }

    @Override
    public void remove() {
        if (this.amount <= removeLimit) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, removeLimit));
        }
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.Wind;
    }
}
