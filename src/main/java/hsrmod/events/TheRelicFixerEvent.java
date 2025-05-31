package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NeowsLament;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.starter.WaxOfElation;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TheRelicFixerEvent extends PhasedEvent {
    public static final String ID = TheRelicFixerEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int fix1Cost = 10;
    int fixAllCost = 75;
    
    public TheRelicFixerEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        fix1Cost = ModHelper.eventAscension() ? 25 : 10;
        fixAllCost = ModHelper.eventAscension() ? 100 : 75;
        
        TextPhase phase0 = new TextPhase(DESCRIPTIONS[0]);
        phase0.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[0], fix1Cost))
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(fix1Cost);
                    List<AbstractRelic> relics = AbstractDungeon.player.relics.stream()
                            .filter(r -> r.usedUp && !Objects.equals(r.relicId, NeowsLament.ID))
                            .collect(Collectors.toList());
                    if (!relics.isEmpty()) {
                        AbstractRelic relic = GeneralUtil.getRandomElement(relics, AbstractDungeon.eventRng);
                        if (relic != null) {
                            ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(false, relic));
                            ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(relic.relicId));
                        }
                    }
                    transitionKey(1);
                })
                .enabledCondition(() -> AbstractDungeon.player.gold >= fix1Cost)
        );
        phase0.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[1], fixAllCost))
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(fixAllCost);
                    AbstractRelic[] relicsArray = AbstractDungeon.player.relics.stream()
                            .filter(r -> r.usedUp && !Objects.equals(r.relicId, NeowsLament.ID))
                            .toArray(AbstractRelic[]::new);
                    ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(false, relicsArray));
                    ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(Arrays.stream(relicsArray).map(r -> r.relicId).toArray(String[]::new)));
                    transitionKey(1);
                })
                .enabledCondition(() -> AbstractDungeon.player.gold >= fixAllCost)
        );
        
        if (ModHelper.hasRelic(WaxOfElation.ID)) {
            phase0.addOption(OPTIONS[3], (i) -> {
                AbstractRelic[] relicsArray = AbstractDungeon.player.relics.stream().filter(r -> r.tier == AbstractRelic.RelicTier.UNCOMMON).toArray(AbstractRelic[]::new);
                ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(true, relicsArray));
                ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(relicsArray.length, r -> RelicTagField.destructible.get(r)));
            });
        }
        
        phase0.addOption(OPTIONS[4], (i) -> openMap());
        
        registerPhase(0, phase0);
        
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[5], (i) -> openMap()));

        transitionKey(0);
    }
}
