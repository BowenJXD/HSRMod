package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ShoppingChannelEvent extends PhasedEvent {
    public static final String ID = ShoppingChannelEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int chance = 80;
    int hpGain = 20;
    int hpLoss = 20; // %
    int hpLoss2 = 4;
    boolean pass = false;

    public ShoppingChannelEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        hpLoss = ModHelper.eventAscension() ? 25 : 20;
        hpLoss2 = ModHelper.eventAscension() ? 10 : 4;
        pass = AbstractDungeon.eventRng.random(99) < chance;

        registerPhase(Phase.START, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[1], (i) -> transitionKey(Phase.PAD))
                .addOption(OPTIONS[2], (i) -> transitionKey(Phase.KICK))
        );
        registerPhase(Phase.PAD, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[0], (i) -> transitionKey(Phase.CHOOSE))
        );
        registerPhase(Phase.KICK, new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[0], (i) -> transitionKey(Phase.CHOOSE))
        );

        TextPhase choosePhase = new TextPhase(DESCRIPTIONS[3]);
        choosePhase.addOption(OPTIONS[3], (i) -> transitionKey(Phase.DONUT));
        choosePhase.addOption(OPTIONS[4], (i) -> transitionKey(Phase.LOTUS));
        choosePhase.addOption(OPTIONS[5], (i) -> transitionKey(Phase.BOX));
        if (ModHelper.hasRelic(WaxOfDestruction.ID)) {
            choosePhase.addOption(GeneralUtil.tryFormat(OPTIONS[6], hpLoss2), (i) -> {
                AbstractDungeon.player.damage(new DamageInfo(null, hpLoss2, DamageInfo.DamageType.HP_LOSS));
                RelicEventHelper.rewardRelics(2);
                transitionKey(Phase.END);
            });
        }
        registerPhase(Phase.CHOOSE, choosePhase);

        TextPhase donutPhase = new TextPhase(DESCRIPTIONS[4]);
        if (pass) {
            int hpGainAmt = AbstractDungeon.player.maxHealth * hpGain / 100;
            donutPhase.addOption(GeneralUtil.tryFormat(OPTIONS[8], hpGainAmt), (i) -> {
                AbstractDungeon.player.heal(hpGainAmt);
                transitionKey(Phase.END);
            });
        } else {
            int hpLossAmt = AbstractDungeon.player.maxHealth * hpLoss / 100;
            donutPhase.addOption(GeneralUtil.tryFormat(OPTIONS[9], hpLossAmt), (i) -> {
                AbstractDungeon.player.damage(new DamageInfo(null, hpLossAmt, DamageInfo.DamageType.HP_LOSS));
                transitionKey(Phase.END);
            });
        }
        registerPhase(Phase.DONUT, donutPhase);

        TextPhase lotusPhase = new TextPhase(DESCRIPTIONS[5]);
        Supplier<CardGroup> cardSupplier = () -> {
            List<AbstractCard> result = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
                    .group.stream().filter(c -> c.rarity == (pass ? AbstractCard.CardRarity.COMMON : AbstractCard.CardRarity.UNCOMMON)).collect(Collectors.toList());
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(result);
            return group;
        };
        lotusPhase.addOption(new TextPhase.OptionInfo(OPTIONS[pass ? 10 : 11]).cardSelectOption(Phase.END, cardSupplier, GeneralUtil.tryFormat(RelicEventHelper.TRANSFORM_TEXT, 1), 1, false, true, false, false, cards -> {
            for (AbstractCard card : cards) {
                card.untip();
                card.unhover();
                AbstractDungeon.player.masterDeck.removeCard(card);
                AbstractDungeon.transformCard(card, true, AbstractDungeon.eventRng);
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && AbstractDungeon.transformedCard != null) {
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getTransformedCard(), Settings.WIDTH * MathUtils.random(0.2f, 0.8f), Settings.HEIGHT * MathUtils.random(0.2f, 0.8f), false));
                }
            }
        }));
        if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.rarity == AbstractCard.CardRarity.COMMON)
                && AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.rarity == AbstractCard.CardRarity.UNCOMMON)) {
            registerPhase(Phase.LOTUS, lotusPhase);
        }
        
        TextPhase boxPhase = new TextPhase(DESCRIPTIONS[6]);
        AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
        AbstractCard curse = AbstractDungeon.returnRandomCurse();
        if (pass) {
            boxPhase.addOption(new TextPhase.OptionInfo(OPTIONS[12], relic), (i) -> {
                RelicEventHelper.gainRelics(relic);
                transitionKey(Phase.END);
            });
        } else {
            boxPhase.addOption(new TextPhase.OptionInfo(OPTIONS[13], curse), (i) -> {
                RelicEventHelper.gainCards(curse);
                transitionKey(Phase.END);
            });
        }
        registerPhase(Phase.BOX, boxPhase);
        registerPhase(Phase.END, new TextPhase(DESCRIPTIONS[7]).addOption(OPTIONS[14], (i) -> openMap()));

        transitionKey(Phase.START);
    }

    enum Phase {
        START,
        PAD,
        KICK,
        CHOOSE,
        DONUT,
        LOTUS,
        BOX,
        END
    }
}
