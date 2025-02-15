package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.relics.starter.WaxOfTheHunt;
import hsrmod.utils.ModHelper;

public class RockPaperScissorsEvent extends PhasedEvent {
    public static final String ID = RockPaperScissorsEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public RockPaperScissorsEvent() {
        super(ID, title, "HSRModResources/img/events/" + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));

        TextPhase phase1 = new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> transitionKey(2))
                .addOption(new TextPhase.OptionInfo(OPTIONS[2])
                        .setOptionResult((i) -> {
                            AbstractDungeon.player.loseGold(100);
                            transitionKey(4);
                        })
                        .enabledCondition(() -> AbstractDungeon.player.gold >= 100));
        if (ModHelper.hasRelic(WaxOfTheHunt.ID)) {
            phase1.addOption(new TextPhase.OptionInfo(OPTIONS[3])
                    .setOptionResult((i) -> transitionKey(3))
                    .enabledCondition(() -> ModHelper.hasRelic(WaxOfTheHunt.ID)));
        }

        registerPhase(1, phase1);
        registerPhase(2, new CombatPhase(Encounter.RPS_1)
                .addRewards(true, null)
                .setNextKey(5)
        );
        registerPhase(3, new CombatPhase(Encounter.RPS_2)
                .addRewards(false, (room) -> {
                    for (int i = 0; i < 2; i++) {
                        RewardItem reward = new RewardItem();
                        reward.cards.forEach(AbstractCard::upgrade);
                        room.addCardReward(reward);
                    }
                })
                .setNextKey(5)
                .setType(AbstractMonster.EnemyType.ELITE)
        );
        registerPhase(4, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[4], (i) -> openMap()));
        registerPhase(5, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[4], (i) -> openMap()));

        transitionKey(0);
    }
}
