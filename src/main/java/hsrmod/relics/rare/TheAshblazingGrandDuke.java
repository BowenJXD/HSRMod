package hsrmod.relics.rare;

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
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class TheAshblazingGrandDuke extends BaseRelic implements OnApplyPowerRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = TheAshblazingGrandDuke.class.getSimpleName();

    public TheAshblazingGrandDuke() {
        super(ID);
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
        ModHelper.findCards((c) -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)).forEach((c) -> c.card.tags.add(FOLLOW_UP));
    }

    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof BrokenPower) {
            Iterator var4 = AbstractDungeon.player.hand.group.iterator();

            AbstractCard c;
            while(var4.hasNext()) {
                c = (AbstractCard) var4.next();
                if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                    addToBot(new FollowUpAction(c));
                }
            }
        }
        return true;
    }
}
