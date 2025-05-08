package androidTestMod.relics.starter;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.misc.ToughnessPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GalacticBat extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = AndroidTestMod.makePath(GalacticBat.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/Trailblazer.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    
    public boolean detailDescription = false;

    String modNameCache = null;
    
    public GalacticBat() {
        super(AndroidTestMod.MOD_NAME, ID, IMG_PATH, RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[detailDescription ? 1 : 0];
    }

    public AbstractRelic makeCopy() {
        return new GalacticBat();
    }

    @Override
    public void atTurnStart() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.hasPower(ToughnessPower.POWER_ID)) {
                addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new ToughnessPower(m), ToughnessPower.getStackLimit(m)));
            }
        }
    }

    @Override
    protected void initializeTips() {
        if (modNameCache == null) {
            modNameCache = tips.get(0).body;
        }
        super.initializeTips();
    }
}
