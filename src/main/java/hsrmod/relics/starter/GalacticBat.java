package hsrmod.relics.starter;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.HeartIsMeantToBeBrokenPower;
import hsrmod.powers.misc.ToughnessPower;

import java.util.ArrayList;

public class GalacticBat extends CustomRelic implements ClickableRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath(GalacticBat.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/Trailblazer.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    
    public boolean detailDescription = false;

    String modNameCache = null;
    
    public GalacticBat() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[detailDescription ? 1 : 0];
    }

    public AbstractRelic makeCopy() {
        return new GalacticBat();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.hasPower(InvinciblePower.POWER_ID)).findFirst()
                .ifPresent(m -> addToTop(new ApplyPowerAction(m, m, new HeartIsMeantToBeBrokenPower(m))));
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

    @Override
    public void onRightClick() {
        flash();
        detailDescription = !detailDescription;
        description = getUpdatedDescription();
        if (!detailDescription) {
            tips = new ArrayList<>(tips.subList(0, 1));
        }
        initializeTips();
        tips.get(0).body = modNameCache;
    }
}
