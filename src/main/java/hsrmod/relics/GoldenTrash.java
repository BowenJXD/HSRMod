package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.ReduceECByHandCardNumAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.EnergyPower;

import java.util.Iterator;

public class GoldenTrash extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath(GoldenTrash.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/Trashcan.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    
    private static int BASE_ENERGY = 0;
    
    private static int ENERGY_GAIN_PER_CARD = 20;
    
    private int multiplier = 10;

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
    public void atBattleStart() {
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, BASE_ENERGY), BASE_ENERGY));
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, ENERGY_GAIN_PER_CARD), ENERGY_GAIN_PER_CARD));
    }

    @Override
    public void onPlayerEndTurn() {
        AbstractPower power = AbstractDungeon.player.getPower(EnergyPower.POWER_ID);
        int maxRetainNum = 0;
        if (power != null){
            maxRetainNum = power.amount / multiplier;
        }

        Iterator var2 = AbstractDungeon.player.hand.group.iterator();
        for (int i = 0 ; i < Math.min(maxRetainNum, AbstractDungeon.player.hand.size()); i++) {
            AbstractCard c = (AbstractCard)var2.next();
            if (!c.isEthereal) {
                c.retain = true;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, -1, false));
        AbstractDungeon.actionManager.addToBottom(new ReduceECByHandCardNumAction(AbstractDungeon.player, AbstractDungeon.player));
    }
}
