package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class CulinaryColosseumEvent extends PhasedEvent {
    public static final String ID = CulinaryColosseumEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int purgeCount = 2;

    public CulinaryColosseumEvent(){
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        purgeCount = ModHelper.eventAscension() ? 1 : 2;
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(GeneralUtil.tryFormat(OPTIONS[2], purgeCount), (i)->transitionKey(3))
                .addOption(OPTIONS[3], (i)->transitionKey(4))
        );
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i)->transitionKey(5)));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[0], (i)->transitionKey(6)));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i)->transitionKey(7)));
        registerPhase(5, new CombatPhase(Encounter.CULINARY_COLOSSEUM_1).addRewards(false, room -> {
            for (int i = 0; i < 2; i++) {
                room.addRelicToRewards(AbstractRelic.RelicTier.COMMON);
            }
        }));
        registerPhase(6, new CombatPhase(Encounter.CULINARY_COLOSSEUM_2).setNextKey(8));
        registerPhase(7, new CombatPhase(Encounter.CULINARY_COLOSSEUM_3).addRewards(false, room->{
            for (int i = 0; i < 3; i++) {
                AbstractRelic relic = RelicEventHelper.getRelicString(r -> RelicTagField.destructible.get(r));
                if (relic != null) room.addRelicToRewards(relic);
                else break;
            }
        }));
        registerPhase(8, new TextPhase(DESCRIPTIONS[5]).addOption(new TextPhase.OptionInfo(OPTIONS[4]).cardRemovalOption(9, OPTIONS[6], purgeCount)));
        registerPhase(9, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[5], (i)->openMap()));
        
        transitionKey(0);
    }
}
