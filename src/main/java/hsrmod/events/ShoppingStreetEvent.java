package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class ShoppingStreetEvent extends PhasedEvent {
    public static final String ID = ShoppingStreetEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int goldGain = 50;
    
    public ShoppingStreetEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldGain = ModHelper.eventAscension() ? 25 : 50;
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> transitionKey(2))
                .addOption(OPTIONS[2], (i) -> transitionKey(2))
        );
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i) -> transitionKey(3)));

        AbstractRelic rareRelic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
        AbstractRelic subtleRelic = RelicEventHelper.getRelics(1, r -> RelicTagField.subtle.get(r)).stream().findAny().orElse(null);
        AbstractRelic economicRelic = RelicEventHelper.getRelics(1, r -> RelicTagField.economic.get(r)).stream().findAny().orElse(null);
        AbstractCard curse = AbstractDungeon.returnRandomCurse();
        AbstractPotion potion = AbstractDungeon.returnRandomPotion();
        
        TextPhase phase3 = new TextPhase(DESCRIPTIONS[3]);
        
        if (rareRelic != null && curse != null) {
            phase3.addOption(new TextPhase.OptionInfo(OPTIONS[3], curse, rareRelic), (i) -> {
                RelicEventHelper.gainCards(curse);
                RelicEventHelper.gainRelics(rareRelic);
                transitionKey(4);
            });
        }
        if (subtleRelic != null && potion != null) {
            phase3.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[4], potion.name), subtleRelic), (i) -> {
                AbstractDungeon.player.obtainPotion(potion);
                RelicEventHelper.gainRelics(subtleRelic);
                transitionKey(4);
            });
        }
        phase3.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[5], goldGain), economicRelic), (i) -> {
            RelicEventHelper.gainGold(goldGain);
            RelicEventHelper.gainRelics(economicRelic);
            transitionKey(4);
        });
        registerPhase(3, phase3);
        
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[6], (i) -> openMap()));
        
        transitionKey(0);
    }
}
