package hsrmod.events;

import basemod.BaseMod;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import basemod.eventUtil.AddEventParams;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.dungeons.Penacony;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class CheatCode1Event extends PhasedEvent {
    public static final String ID = CheatCode1Event.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int goldAmt = 50;
    
    public CheatCode1Event() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldAmt = ModHelper.eventAscension() ? 25 : 50;
        
        AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON);
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(GeneralUtil.tryFormat(OPTIONS[0], goldAmt), (i) -> {
                    RelicEventHelper.gainGold(goldAmt);
                    transitionKey(1);
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[1], relic), (i) -> {
                    RelicEventHelper.gainRelics(relic);
                    addCode2(50);
                    transitionKey(1);
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[2], relic), (i) -> {
                    RelicEventHelper.gainGold(goldAmt);
                    RelicEventHelper.gainRelics(relic);
                    addCode2(100);
                    transitionKey(1);
                })
                .addOption(OPTIONS[3], (i) -> transitionKey(1))
        );
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[4], (i) -> openMap()));
        
        transitionKey(0);
    }
    
    public void addCode2(int chance) {
        if (AbstractDungeon.eventRng.random(99) < chance) {
            BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(CheatCode2Event.ID), CheatCode2Event.class)
                    .dungeonID(Penacony.ID)
                    .endsWithRewardsUI(true)
                    .create());
        }
    }
}
