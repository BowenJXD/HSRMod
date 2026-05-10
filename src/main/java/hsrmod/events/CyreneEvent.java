package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.cardsV2.Abundance.Mydei1;
import hsrmod.cardsV2.Elation.Tribbie2;
import hsrmod.cardsV2.Nihility.Cipher1;
import hsrmod.cardsV2.Nihility.Hysilens1;
import hsrmod.cardsV2.Preservation.PermansorTerrae1;
import hsrmod.cardsV2.Propagation.Anaxa1;
import hsrmod.cardsV2.Propagation.Cerydra1;
import hsrmod.cardsV2.Remembrance.*;
import hsrmod.cardsV2.Trailblaze.Phainon1;
import hsrmod.cardsV2.Trailblaze.Phainon2;
import hsrmod.characters.StellaCharacter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.relics.special.CoreflameOfWorldbearing;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;
import hsrmod.utils.MusicStack;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;

public class CyreneEvent extends PhasedEvent {
    public static final String ID = CyreneEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public CyreneEvent() {
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
                .addOption(OPTIONS[1], (i) -> {
                    transitionKey(3);
                })
                .addOption(OPTIONS[2], (i) -> {
                    transitionKey(4);
                })
                .addOption(OPTIONS[3], (i) -> {
                    transitionKey(5);
                })
        );
        registerPhase(3, new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(6);
                })
        );
        registerPhase(4, new TextPhase(DESCRIPTIONS[4])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(6);
                })
        );
        registerPhase(5, new TextPhase(DESCRIPTIONS[5])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(6);
                })
        );
        registerPhase(6, new TextPhase(DESCRIPTIONS[6])
                .addOption(OPTIONS[4], (i) -> {
                        HSRModConfig.setRandomTP(AbstractDungeon.cardRandomRng);
                        AbstractDungeon.player.energy.energyMaster++;
                    transitionKey(7);
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[5], new Cyrene3()).setOptionResult((i) -> {
                    AbstractCard card = new Cyrene3();
                    card.upgrade();
                    RelicEventHelper.gainCards(card);
                    AbstractDungeon.player.masterMaxOrbs += 2;
                    transitionKey(8);
                }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[6],  new Cyrene3()).setOptionResult((i) -> {
                    AbstractCard[] cards = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c instanceof Cyrene3).toArray(AbstractCard[]::new);
                    RelicEventHelper.purgeCards(cards);
                    RelicEventHelper.upgradeCards(AbstractDungeon.player.masterDeck.group.toArray(new AbstractCard[0]));
                    AbstractCard[] chrysosHeirs = new AbstractCard[]{
                            new Aglaea2(),
                            new Tribbie2(),
                            new Mydei1(),
                            new Castorice3(),
                            new Hyacine1(),
                            new Anaxa1(),
                            new Cipher1(),
                            new Phainon2(),
                            new Hysilens1(),
                            new Cerydra1(),
                            new Evernight1(),
                            new PermansorTerrae1(),
                            new Cyrene2(),
                            new Trailblazer9()
                    };
                    for (AbstractCard chrysosHeir : chrysosHeirs) {
                        chrysosHeir.upgrade();
                        Cyrene4.ChrysosHeirBuff(chrysosHeir);
                    }
                    RelicEventHelper.gainCards(chrysosHeirs);
                    SignatureHelper.unlock(HSRMod.makePath(Cyrene3.ID), true);
                    transitionKey(9);
                }).enabledCondition(() -> AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c instanceof Cyrene3)))
        );
        registerPhase(7, new TextPhase(DESCRIPTIONS[7])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(10);
                })
        );
        registerPhase(8, new TextPhase(DESCRIPTIONS[8])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(10);
                })
        );
        registerPhase(9, new TextPhase(DESCRIPTIONS[9])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(10);
                })
        );
        registerPhase(10, new TextPhase(DESCRIPTIONS[10])
                .addOption(OPTIONS[0], (i) -> {
                    transitionKey(11);
                })
        );
        registerPhase(11, new TextPhase(DESCRIPTIONS[11])
                .addOption(OPTIONS[7], (i) -> {
                    openMap();
                })
        );

        MusicStack.getInstance().push("Past Ripples");
        
        transitionKey(0);
    }
    
    AbstractCard addChrysosHeirMod(AbstractCard card) {
        if (Cyrene4.cardModMap.containsKey(card.cardID)) {
            CardModifierManager.addModifier(card, Cyrene4.cardModMap.get(card.cardID).get());
        }
        card.upgrade();
        return card;
    }
}
