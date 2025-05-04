package hsrmod.relics.common;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NeowsLament;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.*;

public class VoidWickTrimmer extends BaseRelic {
    public static final String ID = VoidWickTrimmer.class.getSimpleName();

    public VoidWickTrimmer() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        List<AbstractRelic> relics = new ArrayList<>();
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r.usedUp && !Objects.equals(r.relicId, NeowsLament.ID)) {
                relics.add(r);
            }
        }
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!relics.isEmpty()) {
            AbstractRelic[] relicsArray = relics.subList(0, Math.min(relics.size(), magicNumber)).toArray(new AbstractRelic[0]);
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    RelicEventHelper.loseRelics(false, relicsArray);
                }
            });
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    for (AbstractRelic relic : relicsArray) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic.makeCopy());
                    }
                }
            });
        }
    }
}
