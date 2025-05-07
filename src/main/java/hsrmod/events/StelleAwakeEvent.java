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
import hsrmod.relics.starter.WaxRelic;
import hsrmod.utils.PathDefine;

import java.util.ArrayList;
import java.util.List;

public class StelleAwakeEvent extends PhasedEvent {
    public static final String ID = StelleAwakeEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public StelleAwakeEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        List<TextPhase.OptionInfo> options = new ArrayList<>();

        for (int i = 0; i < Path.values().length; i++) {
            if (i >= OPTIONS.length - 1) continue;
            Path path = Path.values()[i];
            AbstractRelic relic = path == Path.TRAILBLAZE ? new TrailblazeTimer() : WaxRelic.getWaxRelic(RelicLibrary.starterList, Path.toTag(path));
            if (relic == null) continue;
            AbstractDungeon.player.loseRelic(relic.relicId);
            TextPhase.OptionInfo option = new TextPhase.OptionInfo(OPTIONS[i], relic);
            option.setOptionResult((j) -> {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                // imageEventText.loadImage(PathDefine.EVENT_PATH + ID + "_" + path.name() + ".png");
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
