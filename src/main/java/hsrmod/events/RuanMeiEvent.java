package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.events.exordium.BigFish;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.events.shrines.GoldShrine;
import com.megacrit.cardcrawl.events.shrines.WomanInBlue;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.cards.uncommon.RuanMei1;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RuanMeiEvent extends PhasedEvent {
    public static final String ID = RuanMeiEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    //This text should be set up through loading an event localization json file
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    // private CurScreen screen;
    
    int goldAmt = 1200;
    int relicAmt = 6;

    public RuanMeiEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");
        
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (i)->transitionKey(1))
                .addOption(OPTIONS[1], (i)->transitionKey(2))
                .addOption(OPTIONS[2], (i)->transitionKey(3))
        );
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[4], (i)->transitionKey(4))
                .addOption(OPTIONS[5], (i)->transitionKey(5))
        );
        registerPhase(2, new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[3], (i)->transitionKey(1))
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[3], (i)->transitionKey(1))
        );
        registerPhase(4, new TextPhase(DESCRIPTIONS[4])
                .addOption(OPTIONS[3], (i)->transitionKey(5))
        );
        TextPhase.OptionInfo opt = new TextPhase.OptionInfo(OPTIONS[9])
                .setOptionResult((i) -> {
                    int r = AbstractDungeon.eventRng.random(0, 2);
                    switch (r) {
                        case 0:
                            upgrade(i);
                            gainGold(i);
                            break;
                        case 1:
                            gainGold(i);
                            gainRelics(i);
                            break;
                        case 2:
                            gainRelics(i);
                            upgrade(i);
                            break;
                    }
                    transitionKey(6);
                })
                .enabledCondition(() -> AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.hasTag(CustomEnums.RUAN_MEI)), OPTIONS[10]);
        registerPhase(5, new TextPhase(DESCRIPTIONS[5])
                .addOption(OPTIONS[6], (i) -> {
                    upgrade(i);
                    transitionKey(6);
                })
                .addOption(OPTIONS[7], (i) -> {
                    gainGold(i);
                    transitionKey(6);
                })
                .addOption(OPTIONS[8], (i) -> {
                    gainRelics(i);
                    transitionKey(6);
                })
                .addOption(opt)
        );
        registerPhase(6, new TextPhase(DESCRIPTIONS[5])
                .addOption(OPTIONS[11], (i)->openMap())
        );
        
        //This sets the starting point of the event.
        transitionKey(0);
    }
    
    public void upgrade(Integer i){
        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
        int effectCount = 0;
        List<String> upgradedCards = new ArrayList();
        Iterator var11 = AbstractDungeon.player.masterDeck.group.iterator();

        while (var11.hasNext()) {
            AbstractCard c = (AbstractCard) var11.next();
            if (c.canUpgrade()) {
                ++effectCount;
                if (effectCount <= 20) {
                    float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                }

                upgradedCards.add(c.cardID);
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
            }
        }
    }
    
    public void gainGold(Integer i){
        AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldAmt));
        AbstractDungeon.player.gainGold(this.goldAmt);
    }
    
    public void gainRelics(Integer i){
        for (int i1 = 0; i1 < this.relicAmt; ++i1) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
        }
    }
}