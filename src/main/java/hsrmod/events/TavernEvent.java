package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.utils.ModHelper;

public class TavernEvent extends PhasedEvent {
    public static final String ID = TavernEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    public TavernEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        
        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);
        phase1.addOption(OPTIONS[1], (i)->transitionKey(2));
        phase1.addOption(OPTIONS[2], (i)->transitionKey(3));
        
        if (ModHelper.hasRelic(WaxOfDestruction.ID)) {
            phase1.addOption(new TextPhase.OptionInfo(OPTIONS[3])
                    .setOptionResult((i) -> transitionKey(4))
                    .enabledCondition(() -> ModHelper.hasRelic(WaxOfDestruction.ID))
            );
        }
        
        registerPhase(1, phase1);
        registerPhase(2, new CombatPhase(MonsterHelper.RED_SLAVER_ENC)
                .addRewards(true, (room)->room.addRelicToRewards(AbstractRelic.RelicTier.COMMON))
                .setNextKey(5)
        );
        registerPhase(3, new CombatPhase(MonsterHelper.SHELL_PARASITE_ENC)
                .addRewards(true, (room)->room.addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON))
                .setNextKey(5)
        );
        registerPhase(4, new CombatPhase(Encounter.PARASITE_N_SLAVER)
                .addRewards(true, (room)->room.addRelicToRewards(AbstractRelic.RelicTier.RARE))
                .setNextKey(5)
        );
        registerPhase(5, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[4], (i)->openMap()));
        
        transitionKey(0);
    }
}
