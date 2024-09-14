package hsrmod.relics.uncommon;

import basemod.devcommands.relic.Relic;
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

    public ShiningTrapezohedronDie() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();

        List<AbstractRelic> relics = AbstractDungeon.player.relics.stream().filter(r -> r.tier != RelicTier.STARTER && !Objects.equals(r.relicId, relicId)).collect(Collectors.toList());
        for (AbstractRelic relic : relics) {
            AbstractDungeon.player.loseRelic(relic.relicId);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2),
                    RelicLibrary.getRelic(relic.relicId).makeCopy());
        }
    }
}
