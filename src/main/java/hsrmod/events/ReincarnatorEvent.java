package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.cardsV2.Trailblaze.Phainon1;
import hsrmod.characters.StellaCharacter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.special.CoreflameOfWorldbearing;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

import java.util.List;
import java.util.stream.Collectors;

public class ReincarnatorEvent extends PhasedEvent {
    public static final String ID = ReincarnatorEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public ReincarnatorEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(1);
                })
        );
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(2);
                })
        );
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(OPTIONS[1], new Phainon1(), new CoreflameOfWorldbearing())
                        .setOptionResult((i) -> {
                            RelicEventHelper.gainRelics(new CoreflameOfWorldbearing());
                            RelicEventHelper.gainCards(new Phainon1());
                            transitionKey(3);
                        })
                        .enabledCondition(() -> !AbstractDungeon.player.hasRelic(HSRMod.makePath(CoreflameOfWorldbearing.ID))))
                .addOption(OPTIONS[2], (i) -> {
                    AbstractCard[] cards = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.hasTag(CustomEnums.CHRYSOS_HEIR)).toArray(AbstractCard[]::new);
                    RelicEventHelper.purgeCards(cards);
                    try {
                        Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
                        int count = stellaPref.getInteger("coreflameCount", 0);
                        stellaPref.putInteger("coreflameCount", count + cards.length);
                    } catch (Exception e) {
                        HSRMod.logger.error("Error while loading path", e);
                    }
                    transitionKey(3);
                })
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[3], (i) -> {
                    openMap();
                })
        );
        
        transitionKey(0);
    }
}
