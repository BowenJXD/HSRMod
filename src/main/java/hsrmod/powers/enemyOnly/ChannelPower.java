package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;

public class ChannelPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(ChannelPower.class.getSimpleName());
    public String[] DESCRIPTIONS;
    public PowerStrings powerStrings;
    
    boolean isSurprise;
    
    public ChannelPower(AbstractCreature owner, boolean isSurprise) {
        this.ID = POWER_ID;
        this.name = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
        this.DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
        this.owner = owner;
        this.amount = 0;
        this.type = NeutralPowertypePatch.NEUTRAL;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        this.isTurnBased = true;
        this.canGoNegative = true;
        this.isSurprise = !isSurprise;
        switchChannel();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = isSurprise ? DESCRIPTIONS[0] : DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS) {
            switchChannel();
        }
        return damageAmount;
    }

    public void switchChannel() {
        isSurprise = !isSurprise;
        this.fontScale = 8.0F;
        if (isSurprise) {
            loadRegion("Channel_SurprisePower");
            amount = 1;
        }
        else {
            loadRegion("Channel_FrightPower");
            amount = -1;
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
