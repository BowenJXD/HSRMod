package androidTestMod.relics.common;

import androidTestMod.AndroidTestMod;
import androidTestMod.effects.MergeEffect;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;
import androidTestMod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

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
        ModHelper.addEffectAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                isDone = true;

                List<AbstractRelic> result = new ArrayList<>();
                for (AbstractRelic abstractRelic : AbstractDungeon.player.relics) {
                    if (abstractRelic.tier != RelicTier.STARTER) {
                        result.add(abstractRelic);
                    }
                }
                AbstractRelic[] relics = result.toArray(new AbstractRelic[0]);
                if (relics.length == 0) return;
                AbstractRelic relic = relics[AbstractDungeon.miscRng.random(relics.length - 1)];
                if (relic == RubertEmpireMechanicalPiston.this) {
                    List<AbstractRelic> list = new ArrayList<>();
                    for (AbstractRelic r : relics) {
                        if (r.tier == RelicTier.COMMON || r.tier == RelicTier.UNCOMMON || r.tier == RelicTier.RARE) {
                            list.add(r);
                        }
                    }
                    RelicEventHelper.loseRelics(
                            list.toArray(new AbstractRelic[0])
                    );
                } else {
                    RelicEventHelper.loseRelics(relic);
                }
                RelicEventHelper.gainRelics(relicAmt);
            }
        });
    }

    public void checkMerge() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean merge = p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalCogwheel.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalLever.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalPiston.ID));
        if (merge) {
            boolean b = true;
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof MergeEffect) {
                    b = false;
                    break;
                }
            }
            if (b) {
                AbstractDungeon.effectList.add(new MergeEffect());
            }
        }
    }
}
