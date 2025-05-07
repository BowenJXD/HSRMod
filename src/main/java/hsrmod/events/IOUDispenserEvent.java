package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.common.AngelTypeIOUDispenser;
import hsrmod.relics.starter.WaxOfNihility;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class IOUDispenserEvent extends PhasedEvent {
    public static final String ID = IOUDispenserEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int goldAmount = 100;
    
    public IOUDispenserEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldAmount = ModHelper.eventAscension() ? 75 : 100;

        AbstractRelic r = RelicLibrary.getRelic(HSRMod.makePath(AngelTypeIOUDispenser.ID)).makeCopy();
        TextPhase phase0 = new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (i) -> {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
                    transitionKey(1);
                }, r)
                .addOption(GeneralUtil.tryFormat(OPTIONS[1], goldAmount), (i) -> {
                    RelicEventHelper.gainGold(goldAmount);
                    transitionKey(1);
                });
        if (ModHelper.hasRelic(WaxOfNihility.ID)) {
                phase0.addOption(new TextPhase.OptionInfo(OPTIONS[2])
                        .setOptionResult((i) -> {
                            if (r instanceof AngelTypeIOUDispenser) {
                                ((AngelTypeIOUDispenser) r).magicNumber = 0;
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
                            transitionKey(1);
                        })
                        .enabledCondition(() -> ModHelper.hasRelic(WaxOfNihility.ID))
                );
        }
        registerPhase(0, phase0);
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[3], (i) -> openMap())
        );
        
        transitionKey(0);
    }
}
