package androidTestMod.relics.special;

import androidTestMod.misc.ICanChangeToTempHP;
import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LongevousDisciple extends BaseRelic {
    public static final String ID = LongevousDisciple.class.getSimpleName();

    public LongevousDisciple() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)
                    && c instanceof ICanChangeToTempHP) {
                c.baseBlock++;
                c.block++;
                ((ICanChangeToTempHP) c).changeToTempHP();
            }
        }
    }
}
