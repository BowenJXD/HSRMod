package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FleaMarketEvent extends PhasedEvent {
    public static final String ID = FleaMarketEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int[] costs = new int[]{50, 150, 250};
    int goldGain = 10;
    int destRelicGain = 1;
    int chooseRange = 10;
    int goldGain2 = 200;
    int cardChoose = 4;
    int pathRelicGain = 2;
    int purgeCount = 1;
    int relicGain = 2;
    int healAmt = 13;

    public FleaMarketEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        chooseRange = ModHelper.eventAscension() ? 8 : 10;
        goldGain2 = ModHelper.eventAscension() ? 150 : 200;

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        AbstractCard commonCard = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.eventRng, AbstractCard.CardRarity.COMMON);
        AbstractCard uncommonCard = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.eventRng, AbstractCard.CardRarity.UNCOMMON);
        AbstractCard rareCard = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.eventRng, AbstractCard.CardRarity.RARE);

        AbstractRelic commonRelic = GeneralUtil.getRandomElement(AbstractDungeon.player.relics, AbstractDungeon.eventRng, r -> r.tier == AbstractRelic.RelicTier.COMMON);
        AbstractRelic uncommonRelic = GeneralUtil.getRandomElement(AbstractDungeon.player.relics, AbstractDungeon.eventRng, r -> r.tier == AbstractRelic.RelicTier.UNCOMMON);
        AbstractRelic rareRelic = GeneralUtil.getRandomElement(AbstractDungeon.player.relics, AbstractDungeon.eventRng, r -> r.tier == AbstractRelic.RelicTier.RARE);

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);
        if (commonCard != null || uncommonCard != null || rareCard != null) {
            phase1.addOption(OPTIONS[1], (i) -> transitionKey(2));
        }
        if (commonRelic != null || uncommonRelic != null || rareRelic != null) {
            phase1.addOption(OPTIONS[2], (i) -> transitionKey(3));
        }
        if (AbstractDungeon.player.gold >= costs[0]) {
            phase1.addOption(OPTIONS[3], (i) -> transitionKey(4));
        }
        phase1.addOption(OPTIONS[13], (i) -> openMap());
        registerPhase(1, phase1);

        Supplier<CardGroup> cardGroupSupplier = () -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(AbstractDungeon.rareCardPool.group.stream().filter(c -> !(c instanceof SpawnModificationCard) || ((SpawnModificationCard) c).canSpawn(new ArrayList<>())).collect(Collectors.toList()));
            Collections.shuffle(group.group, new Random(AbstractDungeon.eventRng.randomLong()));
            group.group.subList(Math.min(chooseRange, group.size()), group.size()).clear();
            return group;
        };

        TextPhase phase2 = new TextPhase(DESCRIPTIONS[2]);

        if (commonCard != null) {
            phase2.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[4], commonCard.name, goldGain), commonCard.makeCopy()).setOptionResult((i) -> {
                RelicEventHelper.purgeCards(commonCard);
                RelicEventHelper.gainGold(goldGain);
                transitionKey(5);
            }));
        }
        if (uncommonCard != null) {
            phase2.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[5], uncommonCard.name, destRelicGain), uncommonCard.makeCopy()).setOptionResult((i) -> {
                RelicEventHelper.purgeCards(uncommonCard);
                RelicEventHelper.gainRelics(destRelicGain, r -> RelicTagField.destructible.get(r));
                transitionKey(5);
            }));
        }
        if (rareCard != null) {
            phase2.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[6], rareCard.name, chooseRange), rareCard.makeCopy())
                    .cardSelectOption(5, cardGroupSupplier, GeneralUtil.tryFormat(RelicEventHelper.OBTAIN_TEXT, 1), 1, false, false, true, false, cards -> {
                        AbstractDungeon.effectList.add(new PurgeCardEffect(rareCard, (float) Settings.WIDTH / 3.0F, (float) Settings.HEIGHT / 2.0F));
                        AbstractDungeon.player.masterDeck.removeCard(rareCard);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(cards.get(0), (float) Settings.WIDTH * 2.0F / 3.0F, (float) Settings.HEIGHT / 2.0F));
                    }));
        }
        registerPhase(2, phase2);

        TextPhase phase3 = new TextPhase(DESCRIPTIONS[2]);
        if (commonRelic != null) {
            phase3.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[7], commonRelic.name, goldGain2), commonRelic).setOptionResult((i) -> {
                RelicEventHelper.loseRelics(commonRelic);
                RelicEventHelper.gainGold(goldGain2);
                transitionKey(5);
            }));
        }
        if (uncommonRelic != null) {
            phase3.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[8], uncommonRelic.name, cardChoose), uncommonRelic).setOptionResult((i) -> {
                RelicEventHelper.loseRelics(uncommonRelic);
                RelicEventHelper.addReward(rewards -> {
                    for (int i1 = 0; i1 < cardChoose; i1++) {
                        RewardItem item = new RewardItem();
                        RewardEditor.getInstance().setRewardByPath(item);
                        rewards.add(item);
                    }
                });
                transitionKey(5);
            }));
        }
        if (rareRelic != null) {
            phase3.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[9], rareRelic.name, pathRelicGain), rareRelic).setOptionResult((i) -> {
                RelicEventHelper.loseRelics(rareRelic);
                List<AbstractCard.CardTags> tags = Arrays.asList(CustomEnums.getPathTags());
                Collections.shuffle(tags);
                RelicEventHelper.addReward(rewards -> {
                    tags.stream().filter(t -> t != RewardEditor.getInstance().tag).limit(2).forEach(t -> {
                        String relicId = RelicEventHelper.getRelicByPath(t);
                        RewardItem item = new RewardItem(RelicLibrary.getRelic(relicId));
                        rewards.add(item);
                    });
                });
                transitionKey(5);
            }));
        }
        registerPhase(3, phase3);

        TextPhase phase4 = new TextPhase(DESCRIPTIONS[2]);
        phase4.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[10], costs[0], purgeCount))
                .cardRemovalOption(5, GeneralUtil.tryFormat(RelicEventHelper.PURGE_TEXT, purgeCount), purgeCount)
                .setOptionResult((i) -> AbstractDungeon.player.loseGold(costs[0]))
                .enabledCondition(() -> AbstractDungeon.player.gold >= costs[0]));
        phase4.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[11], costs[1], relicGain))
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(costs[1]);
                    RelicEventHelper.rewardRelics(relicGain);
                    transitionKey(5);
                })
                .enabledCondition(() -> AbstractDungeon.player.gold >= costs[1]));
        phase4.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[12], costs[2], healAmt))
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(costs[2]);
                    AbstractDungeon.player.increaseMaxHp(healAmt, true);
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    transitionKey(5);
                })
                .enabledCondition(() -> AbstractDungeon.player.gold >= costs[2]));
        registerPhase(4, phase4);

        registerPhase(5, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[13], (i) -> openMap()));

        transitionKey(0);
    }
}
