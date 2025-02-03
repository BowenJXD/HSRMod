package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;
import hsrmod.relics.starter.WaxOfErudition;
import hsrmod.relics.uncommon.CosmicBigLotto;
import hsrmod.relics.uncommon.InterastralBigLotto;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;

public class DoubleLotteryEvent extends PhasedEvent {
    public static final String ID = DoubleLotteryEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public DoubleLotteryEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);

        phase1.addOption(new TextPhase.OptionInfo(OPTIONS[1])
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(100);
                    gainLotto();
                    fix();
                    transitionKey(2);
                })
                .enabledCondition(() -> !ModHelper.hasRelic(CosmicBigLotto.ID) || !ModHelper.hasRelic(InterastralBigLotto.ID))
                .enabledCondition(() -> AbstractDungeon.player.gold >= 100));

        phase1.addOption(new TextPhase.OptionInfo(OPTIONS[2])
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(50);
                    fix();
                    transitionKey(3);
                })
                .enabledCondition(() -> {
                    AbstractRelic lotto1 = AbstractDungeon.player.getRelic(HSRMod.makePath(CosmicBigLotto.ID));
                    AbstractRelic lotto2 = AbstractDungeon.player.getRelic(HSRMod.makePath(InterastralBigLotto.ID));
                    return ((lotto1 != null && lotto1.counter == -2) || (lotto2 != null && lotto2.counter == -2));
                }).enabledCondition(() -> AbstractDungeon.player.gold >= 50));

        phase1.addOption(OPTIONS[3], (i) -> transitionKey(4));

        if (ModHelper.hasRelic(WaxOfErudition.ID) && (ModHelper.hasRelic(CosmicBigLotto.ID) || ModHelper.hasRelic(InterastralBigLotto.ID))) {
            phase1.addOption(new TextPhase.OptionInfo(OPTIONS[4]).setOptionResult((i) -> {
                int numLost = loseLotto();
                gainRelics(numLost * 2);
                transitionKey(5);
            }).enabledCondition(() -> ModHelper.hasRelic(WaxOfErudition.ID) && (ModHelper.hasRelic(CosmicBigLotto.ID) || ModHelper.hasRelic(InterastralBigLotto.ID))));
        }

        registerPhase(1, phase1);

        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(5, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[5], (i) -> openMap()));

        transitionKey(0);
    }

    public void gainLotto() {
        List<AbstractRelic> lottos = new ArrayList<>();
        if (!ModHelper.hasRelic(CosmicBigLotto.ID))
            lottos.add(RelicLibrary.getRelic(HSRMod.makePath(CosmicBigLotto.ID)).makeCopy());
        if (!ModHelper.hasRelic(InterastralBigLotto.ID))
            lottos.add(RelicLibrary.getRelic(HSRMod.makePath(InterastralBigLotto.ID)).makeCopy());
        if (!lottos.isEmpty()) {
            AbstractRelic r = lottos.get(AbstractDungeon.eventRng.random(lottos.size() - 1));
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
        }
    }

    public void fix() {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof CosmicBigLotto || relic instanceof InterastralBigLotto) {
                relic.setCounter(1);
                ((BaseRelic) relic).recover();
            }
        }
    }

    public int loseLotto() {
        int result = 0;
        for (int i = AbstractDungeon.player.relics.size() - 1; i >= 0; i--) {
            AbstractRelic relic = AbstractDungeon.player.relics.get(i);
            if (relic instanceof CosmicBigLotto || relic instanceof InterastralBigLotto) {
                RelicEventHelper.loseRelics(relic);
                result++;
            }
        }
        return result;
    }

    public void gainRelics(int amt) {
        for (int i1 = 0; i1 < amt; ++i1) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
        }
    }
}
