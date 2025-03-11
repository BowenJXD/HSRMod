package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Orrery;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfPropagation;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class LonelyBeautyBugsTwoEvent extends PhasedEvent {
    public static final String ID = LonelyBeautyBugsTwoEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    //This text should be set up through loading an event localization json file
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT 
            ? "孤独，太空美虫（其二）" : "Loneliness, Cosmic Beauty Bugs, Simulated Universe (II)";

    public LonelyBeautyBugsTwoEvent() {
        super(ID, NAME, "HSRModResources/img/events/" + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);
        
        phase1.addOption(OPTIONS[1], (i) -> {
            RelicEventHelper.gainRelics(3);
            transitionKey(2);
        });

        phase1.addOption(OPTIONS[2], (i) -> {
            RelicEventHelper.gainRelics(Orrery.ID);
            transitionKey(2);
        });
        
        /*else {
            phase1.addOption(OPTIONS[3], (i) -> {
                AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.BOSS);
                if (r != null)
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
                transitionKey(2);
            });
        }*/
        
        if (ModHelper.hasRelic(WaxOfPropagation.ID)) {
            phase1.addOption(OPTIONS[3], (i) -> {
                AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.BOSS);
                RelicEventHelper.gainRelics(r.relicId);
                transitionKey(2);
            });
        }
        
        phase1.addOption(OPTIONS[4], (i) -> transitionKey(2));

        registerPhase(1, phase1);
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (i) -> openMap()));

        transitionKey(0);
    }
}
