package hsrmod.relics.special;

import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TheAshblazingGrandDuke extends BaseRelic implements OnApplyPowerRelic {
    public static final String ID = TheAshblazingGrandDuke.class.getSimpleName();

    public TheAshblazingGrandDuke() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        ModHelper.findCards((c) -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)).forEach((r) -> r.card.tags.add(FOLLOW_UP));
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
