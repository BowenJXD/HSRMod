package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.RelicEventHelper;

public class TrashSymphonyEvent extends PhasedEvent {
    public static final String ID = TrashSymphonyEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    public TrashSymphonyEvent(){
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(OPTIONS[2], (i)->transitionKey(3)));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i)->transitionKey(4)));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[0], (i)->transitionKey(5)));
        registerPhase(4, new CombatPhase(Encounter.TRASH_SYMPHONY_1).addRewards(false, room -> {
            for (int i = 0; i < 2; i++) {
                room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
            }
        }));
        registerPhase(5, new CombatPhase(Encounter.TRASH_SYMPHONY_2).addRewards(false, room -> {
            for (int i = 0; i < 2; i++) {
                AbstractRelic relic = RelicEventHelper.getRelicString(r -> RelicTagField.destructible.get(r));
                if (relic != null) room.addRelicToRewards(relic);
                else break;
            }
        }));
        registerPhase(6, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[3], (i)->openMap()));
        
        transitionKey(0);
    }
}
