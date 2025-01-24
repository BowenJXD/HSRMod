package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.special.Pineapple;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class PineappleBreadEvent extends BaseEvent {
    public static final String ID = PineappleBreadEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    int purgeCount = 3;
    int relicCount = 2;

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            CardCrawlGame.sound.play("CARD_EXHAUST");
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                logMetricCardRemoval("Purifier", "Purged", (AbstractCard) AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect((AbstractCard) AbstractDungeon.gridSelectScreen.selectedCards.get(i), (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard((AbstractCard) AbstractDungeon.gridSelectScreen.selectedCards.get(i));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    public PineappleBreadEvent() {
        super(ID);

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(2)));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[1], (i) -> {
                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), purgeCount, OPTIONS[2], false, false, false, true);
                    RelicEventHelper.gainRelics(HSRMod.makePath(Pineapple.ID));
                    transitionKey(3);
                }, new Pineapple())
                .addOption(OPTIONS[3], (i) -> {
                    // do nothing
                    transitionKey(4);
                })
                .addOption(OPTIONS[4], (i) -> {
                    RelicEventHelper.gainRelics(relicCount, r -> RelicTagField.subtle.get(r));
                    transitionKey(5);
                })
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(5, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[5], (i) -> openMap()));

        transitionKey(0);
    }
}
