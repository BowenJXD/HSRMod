package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class GeniusSocietyEvent extends PhasedEvent {
    public static final String ID = GeniusSocietyEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int goldAmt = 22;
    int healAmt = 22;
    
    public GeniusSocietyEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldAmt = ModHelper.eventAscension() ? 0 : 22;

        AbstractCard card = AbstractDungeon.returnRandomCard();
        AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[1], (i) -> transitionKey(2)));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[goldAmt > 0 ? 2 : 3], goldAmt), card, relic), (i) -> {
                    RelicEventHelper.gainRelics(relic);
                    RelicEventHelper.gainCards(card);
                    RelicEventHelper.gainGold(goldAmt);
                    transitionKey(3);
                })
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[4], healAmt)), (i) -> {
                    AbstractDungeon.player.heal(healAmt);
                    transitionKey(3);
                })
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        
        transitionKey(0);
    }
}
