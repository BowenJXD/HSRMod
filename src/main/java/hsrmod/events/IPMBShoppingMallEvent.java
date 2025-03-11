package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.common.AmbergrisCheese;
import hsrmod.relics.common.CasketOfInaccuracy;
import hsrmod.relics.common.FruitOfTheAlienTree;
import hsrmod.relics.uncommon.IndecipherableBox;
import hsrmod.relics.uncommon.KingOfSponges;
import hsrmod.relics.uncommon.RottingFruitOfTheAlienTree;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class IPMBShoppingMallEvent extends PhasedEvent {
    public static final String ID = IPMBShoppingMallEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public IPMBShoppingMallEvent() {
        super(ID, NAME, "HSRModResources/img/events/" + ID + ".png");
        
        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);
        
        boolean hasCheese = ModHelper.hasRelic(AmbergrisCheese.ID);
        boolean hasCasket = ModHelper.hasRelic(CasketOfInaccuracy.ID);
        boolean hasFruit = ModHelper.hasRelic(FruitOfTheAlienTree.ID);
        boolean hasSponge = ModHelper.hasRelic(KingOfSponges.ID);
        boolean hasBox = ModHelper.hasRelic(IndecipherableBox.ID);
        boolean hasRotting = ModHelper.hasRelic(RottingFruitOfTheAlienTree.ID);
        
        if (!hasCheese) {
            phase1.addOption(OPTIONS[1], (i) -> {
                RelicEventHelper.gainRelics(HSRMod.makePath(AmbergrisCheese.ID));
                transitionKey(2);
            }, new AmbergrisCheese());
        }
        if (!hasCasket) {
            phase1.addOption(OPTIONS[2], (i) -> {
                RelicEventHelper.gainRelics(HSRMod.makePath(CasketOfInaccuracy.ID));
                transitionKey(2);
            }, new CasketOfInaccuracy());
        }
        if (!hasFruit) {
            phase1.addOption(OPTIONS[3], (i) -> {
                RelicEventHelper.gainRelics(HSRMod.makePath(FruitOfTheAlienTree.ID));
                transitionKey(2);
            }, new FruitOfTheAlienTree());
        }
        if ((hasCheese && !hasSponge)
                || (hasCasket && !hasBox) 
                || (hasFruit && !hasRotting)) {
            phase1.addOption(OPTIONS[4], (i) -> {
                if (hasCheese && !hasSponge) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(AmbergrisCheese.ID));
                    RelicEventHelper.gainRelics(HSRMod.makePath(KingOfSponges.ID));
                }
                if (hasCasket && !hasBox) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(CasketOfInaccuracy.ID));
                    RelicEventHelper.gainRelics(HSRMod.makePath(IndecipherableBox.ID));
                }
                if (hasFruit && !hasRotting) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(FruitOfTheAlienTree.ID));
                    RelicEventHelper.gainRelics(HSRMod.makePath(RottingFruitOfTheAlienTree.ID));
                }
                transitionKey(2);
            });
        }
        if (hasCheese || hasCasket || hasFruit) {
            phase1.addOption(OPTIONS[5], (i) -> {
                int goldAmount = 0;
                if (hasCheese) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(AmbergrisCheese.ID));
                    goldAmount += 200;
                }
                if (hasCasket) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(CasketOfInaccuracy.ID));
                    goldAmount += 200;
                }
                if (hasFruit) {
                    RelicEventHelper.loseRelics(HSRMod.makePath(FruitOfTheAlienTree.ID));
                    goldAmount += 200;
                }
                RelicEventHelper.gainGold(goldAmount);
                transitionKey(2);
            });
        }

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, phase1);
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[6], (i) -> openMap()));
        
        transitionKey(0);
    }
}
