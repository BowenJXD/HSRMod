package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RelicEventHelper;

public class AceTrashDiggerEvent extends BaseEvent {
    public static final String ID = AceTrashDiggerEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    int dRelicCount = 3;
    int nRelicCount = 2;
    
    public AceTrashDiggerEvent() {
        super(ID);
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[1], (i) -> {
                    RelicEventHelper.gainRelics(dRelicCount, r -> RelicTagField.destructible.get(r));
                    for (int j = AbstractDungeon.player.relics.size() - 1; j >= 0; j--) {
                        if (RelicTagField.destructible.get(AbstractDungeon.player.relics.get(j))) {
                            AbstractDungeon.player.relics.get(j).setCounter(-2);
                        }
                    }
                    transitionKey(1);
                })
                .addOption(OPTIONS[2], (i) -> {
                    AbstractRelic relic = GeneralUtil.getRandomElement(AbstractDungeon.player.relics, AbstractDungeon.eventRng, r -> r.tier == AbstractRelic.RelicTier.COMMON);
                    if (relic != null) {
                        RelicEventHelper.loseRelics(relic);
                    }
                    RelicEventHelper.gainRelics(nRelicCount);
                    transitionKey(1);
                })
                .addOption(OPTIONS[3], (i) -> {
                    openMap();
                })
        );
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> openMap()));
        
        transitionKey(0);
    }
}
