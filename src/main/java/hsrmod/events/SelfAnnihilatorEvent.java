package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfErudition;
import hsrmod.relics.starter.WaxOfTheHunt;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

public class SelfAnnihilatorEvent extends PhasedEvent {
    public static final String ID = SelfAnnihilatorEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int goldGain = 50;
    int upgradeCount = 4;

    public SelfAnnihilatorEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        goldGain = ModHelper.eventAscension() ? 25 : 50;

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        AbstractCard curse = AbstractDungeon.returnRandomCurse();

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1]);
        phase1.addOption(new TextPhase.OptionInfo(OPTIONS[1], curse), (i) -> {
            RelicEventHelper.addReward(rewards -> {
                RewardItem reward = new RewardItem();
                RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.RARE);
                RewardEditor.getInstance().setRewardByPath(reward);
                rewards.add(reward);
            });
            RelicEventHelper.gainCards(curse);
            transitionKey(2);
        });
        phase1.addOption(GeneralUtil.tryFormat(OPTIONS[2], goldGain), (i) -> {
            RelicEventHelper.gainGold(goldGain);
            transitionKey(3);
        });
        if (ModHelper.hasRelic(WaxOfTheHunt.ID)) {
            phase1.addOption(new TextPhase.OptionInfo(OPTIONS[3]), (i) -> {
                transitionKey(4);
            });
        }
        if (ModHelper.hasRelic(WaxOfErudition.ID)) {
            phase1.addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[4], upgradeCount), curse), (i) -> {
                RelicEventHelper.gainCards(curse);
                AbstractCard[] eruditionCards = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.hasTag(CustomEnums.ERUDITION) && c.canUpgrade()).limit(upgradeCount).toArray(AbstractCard[]::new);
                RelicEventHelper.upgradeCards(eruditionCards);
                transitionKey(2);
            });
        }
        registerPhase(1, phase1);

        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(4, new CombatPhase(Encounter.SELF_ANNIHILATOR).addRewards(false, room->{
            RewardItem reward = new RewardItem();
            reward.cards.subList(1, reward.cards.size()).clear();
            RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.RARE);
            RewardEditor.getInstance().setRewardByPath(CustomEnums.THE_HUNT, reward, true);
            room.rewards.add(reward);
        }));

        transitionKey(0);
    }
}
