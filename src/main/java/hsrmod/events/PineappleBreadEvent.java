package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.special.Pineapple;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class PineappleBreadEvent extends PhasedEvent {
    public static final String ID = PineappleBreadEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int purgeCount = 3;
    int relicCount = 2;

    public PineappleBreadEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        purgeCount = ModHelper.eventAscension() ? 2 : 3;
        relicCount = ModHelper.eventAscension() ? 1 : 2;

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(2)));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[1], purgeCount), new Pineapple())
                        .cardRemovalOption(3, GeneralUtil.tryFormat(OPTIONS[2], purgeCount), purgeCount)
                        .setOptionResult((i) -> RelicEventHelper.gainRelics(HSRMod.makePath(Pineapple.ID))))
                .addOption(OPTIONS[3], (i) -> {
                    // do nothing
                    transitionKey(4);
                })
                .addOption(GeneralUtil.tryFormat(OPTIONS[4], relicCount), (i) -> {
                    RelicEventHelper.gainRelics(relicCount, r -> RelicTagField.subtle.get(r));
                    transitionKey(5);
                })
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(5, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[5], (i) -> openMap()));

        transitionKey(0);
    }
}
