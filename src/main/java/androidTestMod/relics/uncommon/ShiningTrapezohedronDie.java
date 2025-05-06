package androidTestMod.relics.uncommon;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public class ShiningTrapezohedronDie extends BaseRelic {
    public static final String ID = ShiningTrapezohedronDie.class.getSimpleName();
    
    public ShiningTrapezohedronDie() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();

        List<AbstractRelic> list = new ArrayList<>();
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r.tier == RelicTier.COMMON || r.tier == RelicTier.UNCOMMON || r.tier == RelicTier.RARE) {
                list.add(r);
            }
        }
        AbstractRelic[] relics = list.toArray(new AbstractRelic[0]);
        if (relics.length > 0) {
            RelicEventHelper.loseRelicsAfterwards(relics);
            RelicEventHelper.gainRelicsAfterwards(relics.length);
        }
    }
}
