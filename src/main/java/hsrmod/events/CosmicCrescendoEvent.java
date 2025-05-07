package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfElation;
import hsrmod.relics.starter.WaxRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CosmicCrescendoEvent extends PhasedEvent {
    public static final String ID = CosmicCrescendoEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public int count = 0;
    public String overrideBody = null;

    public CosmicCrescendoEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        TextPhase phase0 = new TextPhase(DESCRIPTIONS[0]);
        phase0.addOption(OPTIONS[1], (i) -> {
            count = 10;
            transitionToRandom();
        });
        phase0.addOption(OPTIONS[2], (i) -> openMap());

        if (ModHelper.hasRelic(WaxOfElation.ID)) {
            phase0.addOption(new TextPhase.OptionInfo(OPTIONS[3]).setOptionResult((i) -> {
                count = 15;
                transitionToRandom();
            }).enabledCondition(() -> ModHelper.hasRelic(WaxOfElation.ID)));
        }

        registerPhase(0, phase0);
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(4, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(5, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(6, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(7, new TextPhase(DESCRIPTIONS[7]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(8, new TextPhase(DESCRIPTIONS[8]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(9, new TextPhase(DESCRIPTIONS[9]).addOption(OPTIONS[0], (i) -> transitionToRandom()));
        registerPhase(10, new TextPhase(DESCRIPTIONS[10]).addOption(OPTIONS[4], (i) -> openMap()));

        transitionKey(0);
    }

    @Override
    public void update() {
        super.update();
        if (overrideBody != null && !Objects.equals(body, overrideBody)) {
            body = overrideBody;
            this.imageEventText.show(NAME, this.body);
        }
    }

    public void transitionToRandom() {
        overrideBody = null;
        if (count <= 0) {
            transitionKey(10);
            return;
        }
        count--;

        boolean good = AbstractDungeon.eventRng.random(99) <= (ModHelper.eventAscension() ? 60 : 66);
        Effect effect = Effect.values()[AbstractDungeon.eventRng.random(Effect.values().length - 1)];
        switch (effect) {
            case GOLD:
                int amount1 = AbstractDungeon.eventRng.random(50, 100);
                if (good) {
                    // Gain gold in fixed amount
                    AbstractDungeon.player.gainGold(amount1);
                    transitionKey(1);
                    overrideBody = String.format(DESCRIPTIONS[1], amount1);
                } else {
                    // Lose gold in fixed amount
                    AbstractDungeon.player.loseGold(amount1);
                    transitionKey(2);
                    overrideBody = String.format(DESCRIPTIONS[2], amount1);
                }
                break;
            case GOLD_PER:
                int percentage = AbstractDungeon.eventRng.random(25, 50);
                int amount2 = AbstractDungeon.player.gold * percentage / 100;
                if (good) {
                    // Gain gold in percentage
                    AbstractDungeon.player.gainGold(amount2);
                    transitionKey(1);
                    overrideBody = String.format(DESCRIPTIONS[1], amount2);
                } else {
                    // Lose gold in percentage
                    AbstractDungeon.player.loseGold(amount2);
                    transitionKey(2);
                    overrideBody = String.format(DESCRIPTIONS[2], amount2);
                }
                break;
            case RELIC:
                if (good) {
                    // Gain random relic
                    AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
                    transitionKey(3);
                    overrideBody = String.format(DESCRIPTIONS[3], r.name);
                } else {
                    // Lose random non-starter relic
                    List<AbstractRelic> relics = AbstractDungeon.player.relics.stream().filter(r -> r.tier != AbstractRelic.RelicTier.STARTER).collect(Collectors.toList());
                    AbstractRelic r;
                    if (!relics.isEmpty()) {
                        r = relics.get(AbstractDungeon.eventRng.random(relics.size() - 1));
                        RelicEventHelper.loseRelics(r);
                        transitionKey(4);
                        overrideBody = String.format(DESCRIPTIONS[4], r.name);
                    } else {
                        transitionKey(9);
                    }
                }
                break;
            case CARD1:
                if (good) {
                    // Gain card from the same path
                    AbstractCard card = null;
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    AbstractCard.CardTags tag = null;
                    if (AbstractDungeon.eventRng.random(99) < 50)
                        tag = WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics);
                    if (tag != null) {
                        group.group.addAll(AbstractDungeon.commonCardPool.group.stream().filter((c) -> c.hasTag(WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics))).collect(Collectors.toList()));
                        group.group.addAll(AbstractDungeon.uncommonCardPool.group.stream().filter((c) -> c.hasTag(WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics))).collect(Collectors.toList()));
                        group.group.addAll(AbstractDungeon.rareCardPool.group.stream().filter((c) -> c.hasTag(WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics))).collect(Collectors.toList()));
                        if (!group.isEmpty()) {
                            card = group.getRandomCard(true);
                        }
                    } else {
                        card = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
                    }
                    if (card != null) {
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                        transitionKey(3);
                        overrideBody = String.format(DESCRIPTIONS[3], card.name);
                    }
                    transitionKey(9);
                } else {
                    // Lose random card
                    AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(true);
                    AbstractDungeon.effectList.add(new PurgeCardEffect(card));
                    AbstractDungeon.player.masterDeck.removeCard(card);
                    transitionKey(4);
                    overrideBody = String.format(DESCRIPTIONS[4], card.name);
                }
                break;
            case CARD2:
                AbstractCard card = null;
                if (good) {
                    // Upgrade random card                    
                    CardGroup group = AbstractDungeon.player.masterDeck.getUpgradableCards();
                    if (!group.isEmpty()) {
                        card = group.getRandomCard(true);
                        if (card != null) {
                            AbstractDungeon.player.bottledCardUpgradeCheck(card);
                            card.upgrade();
                            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard) card).makeStatEquivalentCopy(),
                                    (float) Settings.WIDTH / 2, (float) Settings.HEIGHT / 2));
                            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            transitionKey(5);
                            overrideBody = String.format(DESCRIPTIONS[5], card.name);
                        }
                    }
                } else {
                    // Transform random card
                    CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
                    if (!group.isEmpty()) {
                        card = group.getRandomCard(true);
                        if (card != null) {
                            AbstractDungeon.player.masterDeck.removeCard(card);
                            AbstractDungeon.transformCard(card, true, AbstractDungeon.eventRng);
                            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.getTransformedCard(), (float) Settings.WIDTH * 3 / 4.0F, (float) Settings.HEIGHT / 2.0F));
                            transitionKey(6);
                            overrideBody = String.format(DESCRIPTIONS[6], card.name);
                        }
                    }
                }
                if (card == null) {
                    transitionKey(9);
                }
                break;
            case POTION:
                if (good) {
                    // Gain random potion
                    if (AbstractDungeon.player.potions.stream().anyMatch(p -> p instanceof PotionSlot)) {
                        AbstractPotion potion = AbstractDungeon.returnRandomPotion();
                        AbstractDungeon.player.obtainPotion(potion);
                        transitionKey(3);
                        overrideBody = String.format(DESCRIPTIONS[3], potion.name);
                    } else {
                        transitionKey(9);
                    }
                } else {
                    // Lose random potion
                    if (!AbstractDungeon.player.potions.stream().allMatch(p -> p instanceof PotionSlot)) {
                        List<AbstractPotion> potions = AbstractDungeon.player.potions.stream()
                                .filter(p -> !(p instanceof PotionSlot)).collect(Collectors.toList());
                        AbstractPotion potion = potions.get(AbstractDungeon.eventRng.random(potions.size() - 1));
                        AbstractDungeon.player.removePotion(potion);
                        transitionKey(4);
                        overrideBody = String.format(DESCRIPTIONS[4], potion.name);
                    } else {
                        transitionKey(9);
                    }
                }
                break;
            case HEALTH:
                int percentage2 = AbstractDungeon.eventRng.random(10, 33);
                int amount3 = AbstractDungeon.player.currentHealth * percentage2 / 100;
                if (good) {
                    // Heal in percentage
                    AbstractDungeon.player.heal(amount3);
                    transitionKey(7);
                    overrideBody = String.format(DESCRIPTIONS[7], amount3);
                } else {
                    // Damage in percentage
                    AbstractDungeon.player.damage(new DamageInfo((AbstractCreature) null, amount3, DamageInfo.DamageType.HP_LOSS));
                    transitionKey(8);
                    overrideBody = String.format(DESCRIPTIONS[8], amount3);
                }
                break;
            default:
                transitionKey(9);
        }
    }

    static enum Effect {
        GOLD, // good: Gain (fixed amount), bad: Lose (fixed amount)
        GOLD_PER, // good: Ga in (in percentage), bad: Lose (in percentage)
        RELIC, // good: Gain, bad: Lose
        CARD1, // good: Gain (same path) , bad: Lose
        CARD2, // good: Upgrade, bad: Transform
        POTION, // good: Gain, bad: Lose
        HEALTH; // good: Heal (in percentage), bad: Damage (in percentage)
    }
}
