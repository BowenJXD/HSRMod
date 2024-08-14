package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Vajra;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.EnergyCounter;

public class GoldenTrash extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath("GoldenTrash");
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/Trashcan.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    
    private static int ENERGY_GAIN_PER_CARD = 20;

    public GoldenTrash() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GoldenTrash();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        m.type = AbstractMonster.EnemyType.BOSS;
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyCounter(AbstractDungeon.player, ENERGY_GAIN_PER_CARD), ENERGY_GAIN_PER_CARD));
    }
    
    
}
