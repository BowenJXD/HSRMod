package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.relics.starter.WaxOfErudition;
import hsrmod.relics.starter.WaxOfNihility;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

public class WeAreCowboysEvent extends PhasedEvent {
    public static final String ID = WeAreCowboysEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int extraGold = 0;
    int upgradeCount = 4;

    public WeAreCowboysEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        TextPhase phase0 = new TextPhase(DESCRIPTIONS[0]);
        phase0.addOption(new TextPhase.OptionInfo(OPTIONS[0])
                .setOptionResult((i) -> {
                    AbstractDungeon.player.loseGold(AbstractDungeon.player.gold / 2);
                    transitionKey(1);
                })
        );
        phase0.addOption(new TextPhase.OptionInfo(OPTIONS[1])
                .setOptionResult((i) -> transitionKey(3))
        );

        if (ModHelper.hasRelic(WaxOfNihility.ID)) {
            phase0.addOption(new TextPhase.OptionInfo(OPTIONS[2])
                    .setOptionResult((i) -> {
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold / 4);
                        RelicEventHelper.addReward(rewards -> {
                            for (int i1 = 0; i1 < 2; i1++) {
                                RewardItem reward = new RewardItem();
                                RewardEditor.getInstance().setRewardByPath(CustomEnums.NIHILITY, reward, true);
                                rewards.add(reward);
                            }
                        });
                        transitionKey(2);
                    })
            );
        }
        if (ModHelper.hasRelic(WaxOfDestruction.ID)) {
            phase0.addOption(new TextPhase.OptionInfo(OPTIONS[3])
                    .setOptionResult((i) -> {
                        extraGold = 100;
                        transitionKey(3);
                    })
            );
        }
        if (ModHelper.hasRelic(WaxOfErudition.ID)) {
            phase0.addOption(new TextPhase.OptionInfo(OPTIONS[4])
                    .setOptionResult((i) -> {
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold / 2);
                        RelicEventHelper.upgradeCards(upgradeCount);
                        transitionKey(2);
                    })
            );
        }

        registerPhase(0, phase0);
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[5], (i) -> openMap()));
        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (i) -> openMap()));

        String encounterName = "";
        switch (AbstractDungeon.eventRng.random(2)) {
            case 0:
                encounterName = Encounter.WE_ARE_COWBOYS_1;
                break;
            case 1:
                encounterName = Encounter.WE_ARE_COWBOYS_2;
                break;
            case 2:
                encounterName = Encounter.WE_ARE_COWBOYS_3;
                break;
        }

        registerPhase(3, new CombatPhase(encounterName).addRewards(true, room -> {
            if (extraGold > 0)
                room.rewards.add(new RewardItem(extraGold));
        }));

        transitionKey(0);
    }
}
