package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.RelicEventHelper;

public class CulinaryColosseumEvent extends PhasedEvent {
    public static final String ID = CulinaryColosseumEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    int purgeCount = 2;

    public CulinaryColosseumEvent(){
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(OPTIONS[2], (i)->transitionKey(3))
                .addOption(OPTIONS[3], (i)->transitionKey(4))
        );
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i)->transitionKey(5)));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[0], (i)->transitionKey(6)));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i)->transitionKey(7)));
        registerPhase(5, new CombatPhase(Encounter.CULINARY_COLOSSEUM_1).addRewards(false, room -> {
            for (int i = 0; i < 2; i++) {
                room.addRelicToRewards(AbstractRelic.RelicTier.COMMON);
            }
        }));
        registerPhase(6, new CombatPhase(Encounter.CULINARY_COLOSSEUM_2).setNextKey(8));
        registerPhase(7, new CombatPhase(Encounter.CULINARY_COLOSSEUM_3).addRewards(false, room->{
            for (int i = 0; i < 3; i++) {
                AbstractRelic relic = RelicEventHelper.getRelicString(r -> RelicTagField.destructible.get(r));
                if (relic != null) room.addRelicToRewards(relic);
                else break;
            }
        }));
        registerPhase(8, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[4], (i)-> {
            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), purgeCount, OPTIONS[6], false, false, false, true);
            transitionKey(9);
        }));
        registerPhase(9, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[5], (i)->openMap()));
        
        transitionKey(0);
    }
    
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
}
