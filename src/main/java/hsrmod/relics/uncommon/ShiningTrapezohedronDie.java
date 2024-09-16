/*
package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShiningTrapezohedronDie extends BaseRelic {
    public static final String ID = ShiningTrapezohedronDie.class.getSimpleName();

    List<AbstractRelic> toAdd = new ArrayList<>();
    
    public ShiningTrapezohedronDie() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        
        List<AbstractRelic> relics = AbstractDungeon.player.relics.stream().filter(r -> r.tier != RelicTier.STARTER && !Objects.equals(r.relicId, relicId)).collect(Collectors.toList());
        toAdd = new ArrayList<>(relics);
        for (AbstractRelic relic : relics) {
            AbstractDungeon.player.loseRelic(relic.relicId);
        }
    }

    @Override
    public void update() {
        if (toAdd != null && !toAdd.isEmpty()) {
            AbstractRelic relic = toAdd.get(0);
            if (relic == null) return;
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2),
                    RelicLibrary.getRelic(relic.relicId).makeCopy());
            toAdd.remove(0);
        }
        super.update();
    }
}
*/
