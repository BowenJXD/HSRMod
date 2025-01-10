package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

public class ThreeLittlePigsEvent extends PhasedEvent {
    public static final String ID = ThreeLittlePigsEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    public ThreeLittlePigsEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(OPTIONS[2], (i)->transitionKey(3)));
        registerPhase(2, new CombatPhase(Encounter.THREE_LIL_PIGS).setNextKey(3).addRewards(false, room -> {}));
        registerPhase(3, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[3], (i)->openMap()));
        
        transitionKey(0);
    }
}
