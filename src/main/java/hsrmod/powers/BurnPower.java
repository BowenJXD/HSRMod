package hsrmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import hsrmod.modcore.HSRMod;

public class BurnPower extends DoTPower{
    public static final String POWER_ID = HSRMod.makePath(BurnPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int damage = 5;

    public BurnPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(owner, source, Amount);
        this.name = NAME;
        this.ID = POWER_ID;

        String path128 = "HSRModResources/img/powers/BreakEffect128.png";
        String path48 = "HSRModResources/img/powers/BreakEffect48.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], getDamage());
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
