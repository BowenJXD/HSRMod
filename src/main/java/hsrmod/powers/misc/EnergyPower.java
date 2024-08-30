package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;

public class EnergyPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(EnergyPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public static final int AMOUNT_LIMIT = 240;

    public EnergyPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], AMOUNT_LIMIT);
    }

    @Override
    public void stackPower(int stackAmount) {
        int newValue = amount + stackAmount;

        // Ensure the new value does not exceed bounds
        if (newValue > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        } else if (newValue < 0) {
            amount = 0;
        } else {
            amount = newValue;
        }
    }
}
