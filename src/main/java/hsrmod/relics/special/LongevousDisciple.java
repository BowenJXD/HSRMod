package hsrmod.relics.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.misc.ICanChangeToTempHP;
import hsrmod.relics.BaseRelic;

public class LongevousDisciple extends BaseRelic {
    public static final String ID = LongevousDisciple.class.getSimpleName();

    public LongevousDisciple() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        AbstractDungeon.player.drawPile.group.stream()
                .filter(c -> c.hasTag(AbstractCard.CardTags.STARTER_DEFEND) 
                        && c instanceof ICanChangeToTempHP)
                .forEach(c -> {
                    c.baseBlock++;
                    c.block++;
                    ((ICanChangeToTempHP) c).changeToTempHP();
                });
    }
}
