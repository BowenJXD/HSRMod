package hsrmod.relics.special;

import hsrmod.Hsrmod;
import hsrmod.cards.base.Himeko0;
import hsrmod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HimekoRelic extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = Hsrmod.makePath(HimekoRelic.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/HimekoRelic.png";
    private static final String IMG_PATH_OUTLINE = "HSRModResources/img/relics/outline/HimekoRelic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public HimekoRelic() {
        super(ID,
                AssetLoader.getTexture(Hsrmod.MOD_NAME, IMG_PATH),
                AssetLoader.getTexture(Hsrmod.MOD_NAME, IMG_PATH_OUTLINE),
                RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void onUnequip() {
        super.onUnequip();
        String cardID = Hsrmod.makePath(Himeko0.ID);
        if (AbstractDungeon.player.masterDeck.findCardById(cardID) == null) {
            AbstractCard card = CardLibrary.getCard(cardID).makeCopy();
            card.upgrade();
            RelicEventHelper.gainCards(card);
        }
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new HimekoRelic();
    }
}
