package hsrmod.events;

import basemod.BaseMod;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import basemod.eventUtil.AddEventParams;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfPreservation;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.List;
import java.util.stream.Collectors;

public class LonelyBeautyBugsOneEvent extends PhasedEvent {
    public static final String ID = LonelyBeautyBugsOneEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    //This text should be set up through loading an event localization json file
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT 
            ? "孤独，太空美虫（其一）" : "Loneliness, Cosmic Beauty Bugs, Simulated Universe (I)";

    public LonelyBeautyBugsOneEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> {
                    List<AbstractRelic> relics = AbstractDungeon.player.relics.stream().filter(r -> r.tier != AbstractRelic.RelicTier.STARTER).collect(Collectors.toList());
                    if (!relics.isEmpty()) {
                        AbstractRelic relic = relics.get(AbstractDungeon.cardRandomRng.random(relics.size() - 1));
                        RelicEventHelper.loseRelics(relic);
                    }
                    transition(false);
                })
                .addOption(OPTIONS[2], (i) -> {
                    AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(true);
                    if (card != null) {
                        AbstractDungeon.effectList.add(new PurgeCardEffect(card));
                        AbstractDungeon.player.masterDeck.removeCard(card);
                    }
                    transition(false);
                })
                .addOption(OPTIONS[3], (i) -> {
                    AbstractDungeon.player.loseGold(100);
                    transition(false);
                });
        if (ModHelper.hasRelic(WaxOfPreservation.ID)) {
            phase1.addOption(OPTIONS[4], (i) -> {
                // AbstractDungeon.player.gainGold(100);
                transition(true);
            });
        }
        
        registerPhase(1, phase1);
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        
        transitionKey(0);
    }
    
    public void transition(boolean alwaysPass){
        boolean pass = alwaysPass || AbstractDungeon.eventRng.random(99) < 70;
        if (pass) {
            BaseMod.addEvent(new AddEventParams.Builder(LonelyBeautyBugsTwoEvent.ID, LonelyBeautyBugsTwoEvent.class).bonusCondition(() -> true).create());
            transitionKey(2);
        } else {
            transitionKey(3);
        }
    }
}
