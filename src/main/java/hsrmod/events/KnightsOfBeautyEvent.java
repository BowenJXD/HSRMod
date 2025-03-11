package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.shop.CavitySystemModel;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KnightsOfBeautyEvent extends PhasedEvent {
    public static final String ID = KnightsOfBeautyEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int optionNum = 4;
    int goldGain = 200;
    int pathCardGain = 3;
    int healAmt = 36;
    int relicAmt = 2;

    public KnightsOfBeautyEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        int optionNum = ModHelper.eventAscension() ? 3 : 4;
        int goldGain = ModHelper.eventAscension() ? 150 : 200;
        int healAmt = ModHelper.eventAscension() ? 30 : 36;

        registerPhase(Phase.START, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.START_1)));
        registerPhase(Phase.START_1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.CHOOSE)));

        TextPhase choose = new TextPhase(DESCRIPTIONS[2]);
        TextPhase.OptionInfo stilott = new TextPhase.OptionInfo(OPTIONS[1]).setOptionResult((i) -> {
            AbstractCard[] rareCards = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.rarity == AbstractCard.CardRarity.RARE).toArray(AbstractCard[]::new);
            RelicEventHelper.upgradeCards(rareCards);
            transitionKey(Phase.Stilott);
        });
        TextPhase.OptionInfo abomins = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[2], goldGain)).setOptionResult((i) -> {
            RelicEventHelper.gainGold(goldGain);
            transitionKey(Phase.Abomins);
        });
        TextPhase.OptionInfo argenti = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[3], pathCardGain)).setOptionResult((i) -> {
            RelicEventHelper.addReward(rewards -> {
                for (int i1 = 0; i1 < pathCardGain; i1++) {
                    RewardItem reward = new RewardItem();
                    RewardEditor.getInstance().setRewardByPath(reward, true);
                    rewards.add(reward);
                }
            });
            transitionKey(Phase.Argenti);
        });
        TextPhase.OptionInfo willGarner = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[4], healAmt)).setOptionResult((i) -> {
            AbstractDungeon.player.heal(healAmt);
            transitionKey(Phase.WillGarner);
        });
        TextPhase.OptionInfo pomaine = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[5], relicAmt)).setOptionResult((i) -> {
            RelicEventHelper.rewardRelics(relicAmt);
            transitionKey(Phase.Pomaine);
        });
        TextPhase.OptionInfo anoklay = new TextPhase.OptionInfo(OPTIONS[6]).setOptionResult((i) -> {
            AbstractCard[] curses = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.type == AbstractCard.CardType.CURSE).toArray(AbstractCard[]::new);
            RelicEventHelper.purgeCards(curses);
            transitionKey(Phase.Anoklay);
        });
        TextPhase.OptionInfo holvisio = new TextPhase.OptionInfo(OPTIONS[7]).setOptionResult((i) -> {
            RelicEventHelper.addReward(rewards -> {
                RewardItem reward = new RewardItem();
                RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.RARE);
                RewardEditor.getInstance().setRewardByPath(reward, true);
                rewards.add(reward);
            });
            transitionKey(Phase.Holvisio);
        });
        AbstractRelic cavitySystemModel = RelicLibrary.getRelic(HSRMod.makePath(CavitySystemModel.ID));
        TextPhase.OptionInfo galahadIcahn = null;
        if (cavitySystemModel != null) {
            galahadIcahn = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[8], cavitySystemModel.name), cavitySystemModel).setOptionResult((i) -> {
                RelicEventHelper.gainRelics(cavitySystemModel);
                transitionKey(Phase.GalahadIcahn);
            });
        }
        
        List<TextPhase.OptionInfo> options = new ArrayList<>();
        if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(r -> r.rarity == AbstractCard.CardRarity.RARE))
            options.add(stilott);
        options.add(abomins);
        options.add(argenti);
        if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth)
            options.add(willGarner);
        options.add(pomaine);
        if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.type == AbstractCard.CardType.CURSE))
            options.add(anoklay);
        options.add(holvisio);
        if (!ModHelper.hasRelic(CavitySystemModel.ID) && galahadIcahn != null)
            options.add(galahadIcahn);
        
        Collections.shuffle(options);
        options.stream().limit(optionNum).forEach(choose::addOption);
        registerPhase(Phase.CHOOSE, choose);
        
        registerPhase(Phase.Stilott, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.TEACH)));
        registerPhase(Phase.Abomins, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.GOLD)));
        registerPhase(Phase.Argenti, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.TEACH)));
        registerPhase(Phase.WillGarner, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.CLEANSE)));
        registerPhase(Phase.Pomaine, new TextPhase(DESCRIPTIONS[7]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.RELIC)));
        registerPhase(Phase.Anoklay, new TextPhase(DESCRIPTIONS[8]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.CLEANSE)));
        registerPhase(Phase.Holvisio, new TextPhase(DESCRIPTIONS[9]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.RELIC)));
        registerPhase(Phase.GalahadIcahn, new TextPhase(DESCRIPTIONS[10]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.TEACH)));

        registerPhase(Phase.TEACH, new TextPhase(DESCRIPTIONS[11]).addOption(OPTIONS[9], (i) -> openMap()));
        registerPhase(Phase.RELIC, new TextPhase(DESCRIPTIONS[12]).addOption(OPTIONS[9], (i) -> openMap()));
        registerPhase(Phase.CLEANSE, new TextPhase(DESCRIPTIONS[13]).addOption(OPTIONS[9], (i) -> openMap()));
        registerPhase(Phase.GOLD, new TextPhase(DESCRIPTIONS[14]).addOption(OPTIONS[9], (i) -> openMap()));
        
        transitionKey(Phase.START);
    }

    enum Phase {
        START,
        START_1,
        CHOOSE,
        Stilott,
        Abomins,
        Argenti,
        WillGarner,
        Pomaine,
        Anoklay,
        Holvisio,
        GalahadIcahn,
        TEACH,
        RELIC,
        CLEANSE,
        GOLD
    }
}
