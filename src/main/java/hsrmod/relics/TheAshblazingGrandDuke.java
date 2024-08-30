package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.FollowUpAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.BrokenPower;

import java.util.Iterator;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class TheAshblazingGrandDuke extends CustomRelic implements OnApplyPowerRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath(TheAshblazingGrandDuke.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/TheAshblazingGrandDuke.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public TheAshblazingGrandDuke() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    @Override
    public AbstractRelic makeCopy() {
        return new TheAshblazingGrandDuke();
    }

    @Override
    public void atBattleStart() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
                c.tags.add(FOLLOW_UP);
        }

        var1 = AbstractDungeon.player.drawPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
                c.tags.add(FOLLOW_UP);
        }

        var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
                c.tags.add(FOLLOW_UP);
        }

        var1 = AbstractDungeon.player.exhaustPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
                c.tags.add(FOLLOW_UP);
        }
    }

    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof BrokenPower) {
            Iterator var4 = AbstractDungeon.player.hand.group.iterator();

            AbstractCard c;
            while(var4.hasNext()) {
                c = (AbstractCard) var4.next();
                if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) || c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                    addToBot(new FollowUpAction(c));
                }
            }
        }
        return true;
    }
}
