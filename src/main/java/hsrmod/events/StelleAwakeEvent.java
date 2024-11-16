package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.Path;
import hsrmod.relics.starter.TrailblazeTimer;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.relics.starter.WaxOfElation;
import hsrmod.relics.starter.WaxRelic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StelleAwakeEvent extends PhasedEvent {
    public static final String ID = StelleAwakeEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public StelleAwakeEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        AbstractDungeon.player.loseRelic(HSRMod.makePath(TrailblazeTimer.ID));

        List<TextPhase.OptionInfo> options = new ArrayList<>();

        for (int i = 0; i < Path.values().length; i++) {
            if (i >= OPTIONS.length - 1) continue;
            Path path = Path.values()[i];
            AbstractRelic relic = path == Path.TRAILBLAZE ? new TrailblazeTimer() : WaxRelic.getWaxRelic(RelicLibrary.starterList, Path.toTag(path));
            if (relic == null) continue;
            TextPhase.OptionInfo option = new TextPhase.OptionInfo(OPTIONS[i], relic);
            option.setOptionResult((j) -> {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                // imageEventText.loadImage("HSRModResources/img/events/" + ID + "_" + path.name() + ".png");
                transitionKey(1);
            });
            options.add(option);
        }
        
        TextPhase phase0 = new TextPhase(DESCRIPTIONS[0]);
        options.forEach(phase0::addOption);
        
        registerPhase(0, phase0);
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[OPTIONS.length - 1], (j) -> openMap()));
        
        transitionKey(0);
    }
    
    @Override
    public void onEnterRoom() {
        RoomEventDialog.waitForInput = true;
    }
}
