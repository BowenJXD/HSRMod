package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfPreservation;
import hsrmod.relics.starter.WaxOfTheHunt;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

public class ThreeLittlePigsEvent extends PhasedEvent {
    public static final String ID = ThreeLittlePigsEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    public ThreeLittlePigsEvent() {
        super(ID, NAME, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(OPTIONS[2], (i)->openMap());
        if (ModHelper.hasRelic(WaxOfPreservation.ID)) {
            phase1.addOption(OPTIONS[3], (i)->transitionKey(3));
        }
        if (ModHelper.hasRelic(WaxOfTheHunt.ID)) {
            phase1.addOption(OPTIONS[4], (i)->transitionKey(4));
        }
        registerPhase(1, phase1);
        registerPhase(2, new CombatPhase(Encounter.THREE_LIL_PIGS).addRewards(false, room -> {}));
        registerPhase(3, new CombatPhase(Encounter.THREE_LIL_PIGS_SLOW).addRewards(false, room -> {}));
        registerPhase(4, new CombatPhase(Encounter.THREE_LIL_PIGS_FAST).addRewards(false, room -> {
            RewardEditor.addExtraRewardToTop(rewardItems -> {
                for (RewardItem reward : rewardItems) {
                    if (reward.cards != null) {
                        for (int i = 0; i < reward.cards.size(); i++) {
                            reward.cards.get(i).upgrade();
                        }
                    }
                }
            });
        }));
        
        transitionKey(0);
    }
}
