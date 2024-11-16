package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import basemod.devcommands.relic.Relic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.TheLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.Path;
import hsrmod.relics.starter.WaxRelic;
import hsrmod.utils.RewardEditor;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaxManufacturerEvent extends PhasedEvent {
    public static final String ID = WaxManufacturerEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    //This text should be set up through loading an event localization json file
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    
    public WaxManufacturerEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");

        TextPhase phase = new TextPhase(DESCRIPTIONS[0]);
        
        AbstractCard.CardTags selectedPathTag = WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics);
        Supplier<CardGroup> cardGroupSupplier = () -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(AbstractDungeon.commonCardPool.group.stream().filter((c) -> c.hasTag(selectedPathTag)).collect(Collectors.toList()));
            group.group.addAll(AbstractDungeon.uncommonCardPool.group.stream().filter((c) -> c.hasTag(selectedPathTag)).collect(Collectors.toList()));
            group.group.addAll(AbstractDungeon.rareCardPool.group.stream().filter((c) -> c.hasTag(selectedPathTag)).collect(Collectors.toList()));
            return group;
        };
        if (selectedPathTag != null) {
            TextPhase.OptionInfo opt1 = new TextPhase.OptionInfo(OPTIONS[0])
                    .cardSelectOption(1, cardGroupSupplier, DESCRIPTIONS[2], 1, false, false, false, false, (cards) -> {
                        if (cards.isEmpty()) return;
                        AbstractCard card = cards.get(0);
                        if (card == null) return;
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    });
            phase.addOption(opt1);
        }

        AbstractCard.CardTags mostCommonTag = getMostCommonTag(AbstractDungeon.player.masterDeck);
        WaxRelic relic = WaxRelic.getWaxRelic(RelicLibrary.starterList, mostCommonTag);
        if (selectedPathTag != mostCommonTag && relic != null) {
            TextPhase.OptionInfo opt2 = new TextPhase.OptionInfo(OPTIONS[1], relic).setOptionResult((i) -> {
                for (int j = AbstractDungeon.player.relics.size() - 1; j >= 0; j--) {
                    AbstractRelic r = AbstractDungeon.player.relics.get(j);
                    if (r instanceof WaxRelic && ((WaxRelic) r).tag == selectedPathTag) {
                        AbstractDungeon.player.loseRelic(r.relicId);
                        break;
                    }
                }
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                transitionKey(1);
            });
            phase.addOption(opt2);
        }
        
        else if (selectedPathTag != null) {
            TextPhase.OptionInfo opt3 = new TextPhase.OptionInfo(OPTIONS[2]).setOptionResult((i) -> {
                upgrade(selectedPathTag);
                transitionKey(1);
            });
            phase.addOption(opt3);
        }

        AbstractRelic relic1 = RelicLibrary.getRelic(HSRMod.makePath(RewardEditor.getRelicByPath(mostCommonTag))).makeCopy();
        if (selectedPathTag == null && relic1 != null) {
            TextPhase.OptionInfo opt4 = new TextPhase.OptionInfo(OPTIONS[3], relic1).setOptionResult((i) -> {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic1);
                transitionKey(1);
            });
            phase.addOption(opt4);
        }
        
        registerPhase(0, phase);
        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[4], (i)->openMap())
        );
        transitionKey(0);
    }
    
    public void upgrade(AbstractCard.CardTags tag) {
        ArrayList<AbstractCard> upgradableCards = new ArrayList();
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.canUpgrade() && c.hasTag(tag)) {
                upgradableCards.add(c);
            }
        }

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            for (int i = 0; i < Math.min(3, upgradableCards.size()); i++) {
                AbstractCard card = upgradableCards.get(i);
                AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard) card);
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard) card).makeStatEquivalentCopy(), 
                        AbstractDungeon.miscRng.random(0.1F, 0.9F) * (float) Settings.WIDTH, AbstractDungeon.miscRng.random(0.2F, 0.8F) * (float) Settings.HEIGHT));
            }
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }
    
    public static AbstractCard.CardTags getMostCommonTag(CardGroup group) {
        Map<AbstractCard.CardTags, Integer> tagCount = Stream.of(Path.values())
                .collect(Collectors.toMap(Path::toTag, p -> group.group.stream().mapToInt(card -> card.hasTag(Path.toTag(p)) ? 1 : 0).sum()));
        tagCount.remove(CustomEnums.TRAILBLAZE);
        tagCount.remove(CustomEnums.ABUNDANCE);
        tagCount.remove(CustomEnums.REMEMBRANCE);
        return tagCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }
}
