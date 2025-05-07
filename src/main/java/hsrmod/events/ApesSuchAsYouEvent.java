package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class ApesSuchAsYouEvent extends PhasedEvent {
    public static final String ID = ApesSuchAsYouEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int goldGain = 100;
    int hpLoss = 20;
    int[] headChances = new int[]{40, 30, 30};
    int[] tailChances = new int[]{40, 20, 40};
    int[] tummyChances = new int[]{40, 60};

    public ApesSuchAsYouEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldGain = ModHelper.eventAscension() ? 75 : 100;
        hpLoss = ModHelper.eventAscension() ? 25 : 20;

        registerPhase(Phase.START, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.CHOOSE)));
        registerPhase(Phase.CHOOSE, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> transitionKey(Phase.HEAD))
                .addOption(OPTIONS[2], (i) -> transitionKey(Phase.TAIL))
                .addOption(OPTIONS[3], (i) -> transitionKey(Phase.TUMMY))
                .addOption(OPTIONS[4], (i) -> transitionKey(Phase.LEAVE))
        );

        int r = AbstractDungeon.eventRng.random(99);

        TextPhase.OptionInfo stickyOption = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[6], goldGain)).setOptionResult((i) -> {
            RelicEventHelper.gainGold(goldGain);
            transitionKey(Phase.LEAVE);
        });

        TextPhase.OptionInfo hideOption = new TextPhase.OptionInfo(OPTIONS[7]).setOptionResult((i) -> {
            RelicEventHelper.gainRelics(1);
            transitionKey(Phase.LEAVE);
        });

        TextPhase.OptionInfo bothOption = new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[8], goldGain)).setOptionResult((i) -> {
            RelicEventHelper.gainGold(goldGain);
            RelicEventHelper.gainRelics(1);
            transitionKey(Phase.LEAVE);
        });

        TextPhase headPhase = new TextPhase(DESCRIPTIONS[2]);
        if (r < headChances[0]) {
            headPhase.addOption(OPTIONS[5], (i) -> transitionKey(Phase.NOTHING));
        } else if (r < headChances[0] + headChances[1]) {
            headPhase.addOption(stickyOption);
        } else {
            headPhase.addOption(hideOption);
        }
        registerPhase(Phase.HEAD, headPhase);
        
        TextPhase tailPhase = new TextPhase(DESCRIPTIONS[3]);
        if (r < tailChances[0]) {
            tailPhase.addOption(hideOption);
        } else if (r < tailChances[0] + tailChances[1]) {
            tailPhase.addOption(bothOption);
        } else {
            tailPhase.addOption(OPTIONS[0], (i) -> transitionKey(Phase.HURT));
        }
        registerPhase(Phase.TAIL, tailPhase);
        
        TextPhase tummyPhase = new TextPhase(DESCRIPTIONS[4]);
        if (r < tummyChances[0]) {
            tummyPhase.addOption(bothOption);
        } else {
            tummyPhase.addOption(OPTIONS[0], (i) -> {
                transitionKey(Phase.ROAR);
            });
        }
        registerPhase(Phase.TUMMY, tummyPhase);
        
        registerPhase(Phase.LEAVE, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[11], (i) -> openMap()));
        registerPhase(Phase.NOTHING, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[11], (i) -> openMap()));
        registerPhase(Phase.HURT, new TextPhase(DESCRIPTIONS[7]).addOption(GeneralUtil.tryFormat(OPTIONS[9], hpLoss), (i) -> {
            int dmg = AbstractDungeon.player.currentHealth * hpLoss / 100;
            AbstractDungeon.player.damage(new DamageInfo(null, dmg, DamageInfo.DamageType.HP_LOSS));
            transitionKey(Phase.HURT_AFTER);
        }));
        registerPhase(Phase.ROAR, new TextPhase(DESCRIPTIONS[8]).addOption(GeneralUtil.tryFormat(OPTIONS[10], goldGain), (i) -> transitionKey(Phase.ROAR_AFTER)));
        registerPhase(Phase.HURT_AFTER, new TextPhase(DESCRIPTIONS[9]).addOption(OPTIONS[11], (i) -> openMap()));
        registerPhase(Phase.ROAR_AFTER, new TextPhase(DESCRIPTIONS[10]).addOption(OPTIONS[12], (i) -> transitionKey(Phase.BATTLE)));
        registerPhase(Phase.BATTLE, new CombatPhase(Encounter.APES_SUCH_AS_YOU).addRewards(false, room -> {
            room.rewards.add(new RewardItem(goldGain));
            room.rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));
        }));

        transitionKey(Phase.START);
    }

    enum Phase {
        START,
        CHOOSE,
        HEAD,
        TAIL,
        TUMMY,
        NOTHING,
        LEAVE,
        HURT,
        ROAR,
        HURT_AFTER,
        ROAR_AFTER,
        BATTLE,
    }
}
