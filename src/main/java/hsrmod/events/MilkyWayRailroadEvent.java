package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.cardsV2.NightOnTheMilkyWay;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.RelicEventHelper;

public class MilkyWayRailroadEvent extends PhasedEvent {
    public static final String ID = MilkyWayRailroadEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    public MilkyWayRailroadEvent() {
        super(ID, NAME, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(2)));
        
        Normality normality = new Normality();
        NightOnTheMilkyWay nightOnTheMilkyWay = new NightOnTheMilkyWay();
        
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(OPTIONS[1], normality).setOptionResult((i) -> {
                    RelicEventHelper.gainGold(344);
                    RelicEventHelper.gainCards(normality);
                    transitionKey(3);
                }))
                .addOption(OPTIONS[2], (i) -> {
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    transitionKey(3);
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[3], nightOnTheMilkyWay).setOptionResult((i) -> {
                    RelicEventHelper.gainCards(nightOnTheMilkyWay);
                    transitionKey(3);
                }))
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[4], (i) -> openMap()));
        
        transitionKey(0);
    }
}
