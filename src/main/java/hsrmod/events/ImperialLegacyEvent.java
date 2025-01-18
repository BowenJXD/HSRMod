package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.common.RubertEmpireDifferenceMachine;
import hsrmod.relics.common.RubertEmpireMechanicalCogwheel;
import hsrmod.relics.common.RubertEmpireMechanicalLever;
import hsrmod.relics.common.RubertEmpireMechanicalPiston;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;

public class ImperialLegacyEvent extends PhasedEvent {
    public static final String ID = ImperialLegacyEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    public ImperialLegacyEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(2)));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i) -> transitionKey(3)));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[1], (i) -> transitionKey(4)).addOption(OPTIONS[2], (i) -> transitionKey(4)));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i) -> transitionKey(5)));
        registerPhase(5, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[0], (i) -> transitionKey(6)));
        
        TextPhase phase6 = new TextPhase(DESCRIPTIONS[6]);
        phase6.addOption(OPTIONS[3], (i) -> {
            if (AbstractDungeon.eventRng.random(100) < 10) {
                RelicEventHelper.gainGold(300);
                RelicEventHelper.gainRelicsAfterwards(3);
                RelicEventHelper.upgradeCards(3);
                transitionKey(10);
            } else {
                transitionKey(9);
            }
        });
        List<String> empireRelics = new ArrayList<>();
        if (!ModHelper.hasRelic(RubertEmpireMechanicalPiston.ID)) empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalPiston.ID));
        if (!ModHelper.hasRelic(RubertEmpireMechanicalCogwheel.ID)) empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalCogwheel.ID));
        if (!ModHelper.hasRelic(RubertEmpireMechanicalLever.ID)) empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalLever.ID));
        if (!empireRelics.isEmpty()) {
            phase6.addOption(OPTIONS[4], (i) -> {
                String relicId = empireRelics.get(AbstractDungeon.eventRng.random(empireRelics.size() - 1));
                RelicEventHelper.gainRelics(relicId);
                transitionKey(11);
            });
        }
        if (empireRelics.size() == 3 && AbstractDungeon.player.gold >= 300) {
            phase6.addOption(OPTIONS[5], (i) -> {
                AbstractDungeon.player.loseGold(300);
                empireRelics.remove(AbstractDungeon.eventRng.random(empireRelics.size() - 1));
                RelicEventHelper.gainRelics(empireRelics.toArray(new String[0]));
                transitionKey(12);
            });
        }
        if (ModHelper.hasRelic(RubertEmpireDifferenceMachine.ID)) {
            phase6.addOption(OPTIONS[6], (i) -> {
                RelicEventHelper.loseRelics(AbstractDungeon.player.getRelic(HSRMod.makePath(RubertEmpireDifferenceMachine.ID)));
                RelicEventHelper.gainGold(2400);
                RelicEventHelper.gainRelics(24);
                RelicEventHelper.upgradeCards(24);
                transitionKey(13);
            });
        }
        
        registerPhase(6, phase6);
        registerPhase(7, new TextPhase(DESCRIPTIONS[7]).addOption(OPTIONS[0], (i) -> transitionKey(8)));
        registerPhase(8, new TextPhase(DESCRIPTIONS[8]).addOption(OPTIONS[7], (i) -> openMap()));
        registerPhase(9, new TextPhase(DESCRIPTIONS[9]).addOption(OPTIONS[0], (i) -> transitionKey(7)));
        registerPhase(10, new TextPhase(DESCRIPTIONS[10]).addOption(OPTIONS[0], (i) -> transitionKey(7)));
        registerPhase(11, new TextPhase(DESCRIPTIONS[11]).addOption(OPTIONS[0], (i) -> transitionKey(7)));
        registerPhase(12, new TextPhase(DESCRIPTIONS[12]).addOption(OPTIONS[0], (i) -> transitionKey(7)));
        registerPhase(13, new TextPhase(DESCRIPTIONS[13]).addOption(OPTIONS[0], (i) -> transitionKey(7)));
        
        transitionKey(0);
    }
}
