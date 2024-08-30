package hsrmod.powers.only;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.AftertastePower;
import hsrmod.utils.SubscribeManager;

public class DivinityPower extends AbstractPower implements OnPlayerLoseBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(DivinityPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float blockMultiplier = 2;
    private float loseMultiplier = 1 / 4f;
    
    public DivinityPower(AbstractCreature owner, int Amount, float blockMultiplier, float loseMultiplier) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.blockMultiplier = blockMultiplier;
        this.loseMultiplier = loseMultiplier;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], (int) (blockMultiplier * 100), (int) (loseMultiplier * 100));
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * (1+blockMultiplier);
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (!SubscribeManager.checkSubscriber(this)) return i;
        return (int) (i * loseMultiplier);
    }
}
