package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.TheCity.Cirrus;
import hsrmod.relics.starter.WaxOfPreservation;
import hsrmod.relics.starter.WaxOfTheHunt;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

public class TheReturningHeliobusEvent extends PhasedEvent {
    public static final String ID = TheReturningHeliobusEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    public TheReturningHeliobusEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> transitionKey(2))
                .addOption(OPTIONS[2], (i) -> openMap())
        );
        registerPhase(2, new CombatPhase(Encounter.THE_RETURNING_HELIOBUS).addRewards(false, room->{
            RewardEditor.addExtraRewardToTop(rewards-> {
                int goldAmt = room.monsters.monsters.size() * 10;
                rewards.add(new RewardItem(goldAmt));
            });
        }));
        
        transitionKey(0);
    }
}
