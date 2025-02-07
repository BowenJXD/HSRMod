package hsrmod.relics.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RubertEmpireMechanicalPiston extends BaseRelic implements IRubertEmpireRelic {
    public static final String ID = RubertEmpireMechanicalPiston.class.getSimpleName();
    public int relicAmt = 2;

    public RubertEmpireMechanicalPiston() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        checkMerge();
    }

    @Override
    public void onEnterRestRoom() {
        super.onEnterRestRoom();
        if (usedUp) return;
        ModHelper.addEffectAbstract(() -> {
            isDone = true;

            AbstractRelic[] relics = AbstractDungeon.player.relics.stream().filter(r -> r.tier != AbstractRelic.RelicTier.STARTER).toArray(AbstractRelic[]::new);
            if (relics.length == 0) return;
            AbstractRelic relic = relics[AbstractDungeon.miscRng.random(relics.length - 1)];
            if (relic == RubertEmpireMechanicalPiston.this) {
                RelicEventHelper.loseRelics(
                        Arrays.stream(relics)
                                .filter(r -> r.tier == RelicTier.COMMON || r.tier == RelicTier.UNCOMMON || r.tier == RelicTier.RARE)
                                .toArray(AbstractRelic[]::new)
                );
            } else {
                RelicEventHelper.loseRelics(relic);
            }
            RelicEventHelper.gainRelics(relicAmt);
        });
    }
}
