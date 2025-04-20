package hsrmod.relics.common;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NeowsLament;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.patches.OnRelicDestroyPatch;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.IRunnableSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class VoidWickTrimmer extends BaseRelic {
    public static final String ID = VoidWickTrimmer.class.getSimpleName();

    public VoidWickTrimmer() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        List<AbstractRelic> relics = AbstractDungeon.player.relics.stream().filter(r -> r.usedUp && !Objects.equals(r.relicId, NeowsLament.ID)).collect(Collectors.toList());
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!relics.isEmpty()) {
            AbstractRelic[] relicsArray = relics.subList(0, Math.min(relics.size(), magicNumber)).toArray(new AbstractRelic[0]);
            ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(false, relicsArray));
            ModHelper.addEffectAbstract(() -> {
                for (AbstractRelic relic : relicsArray) {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic.makeCopy());
                }
            });
        }
    }
}
