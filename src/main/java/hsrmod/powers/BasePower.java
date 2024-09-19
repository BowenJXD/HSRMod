package hsrmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

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

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }
    
    public BasePower(String id, AbstractCreature owner, int Amount, PowerType type){
        this(id, owner, Amount, type,false);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[upgraded && DESCRIPTIONS.length > 1 ? 1 : 0];
    }
}
