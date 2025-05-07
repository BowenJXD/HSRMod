package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.special.InsectWeb;
import hsrmod.relics.starter.WaxOfPreservation;
import hsrmod.relics.starter.WaxOfPropagation;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

public class InsectNestEvent extends PhasedEvent {
    public static final String ID = InsectNestEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;
    
    int hpLoss = 20; // %
    int goldGain = 50;
    
    public InsectNestEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");
        
        goldGain = ModHelper.eventAscension() ? 25 : 50;
        
        registerPhase(Phase.START, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[2], (i) -> transitionKey(Phase.INNER1))
                .addOption(OPTIONS[1], (i) -> {
                    RelicEventHelper.gainGold(goldGain);
                    transitionKey(Phase.LEAVE);
                })
        );
        registerPhase(Phase.LEAVE, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[7], (i) -> openMap())
        );
        registerPhase(Phase.INNER1, new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[0], (i) -> transitionKey(Phase.INNER2))
        );
        TextPhase inner2 = new TextPhase(DESCRIPTIONS[3]);
        
        InsectWeb web = new InsectWeb();
        
        inner2.addOption(new TextPhase.OptionInfo(OPTIONS[3], web).setOptionResult((i) -> {
            RelicEventHelper.gainRelics(web.relicId);
            RelicEventHelper.addReward(rewards -> {
                RewardItem reward = new RewardItem();
                RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.RARE);
                RewardEditor.getInstance().setRewardByPath(reward);
                rewards.add(reward);
            });
            transitionKey(Phase.EMBRACE);
        }));
        inner2.addOption(OPTIONS[4], (i) -> {
            int dmg = AbstractDungeon.player.currentHealth * hpLoss / 100;
            AbstractDungeon.player.damage(new DamageInfo(null, dmg, DamageInfo.DamageType.HP_LOSS));
            RelicEventHelper.addReward(rewards -> {
                RewardItem reward = new RewardItem();
                RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.UNCOMMON);
                RewardEditor.getInstance().setRewardByPath(reward);
                rewards.add(reward);
            });
            transitionKey(Phase.WAIT);
        });
        
        if (ModHelper.hasRelic(WaxOfPreservation.ID) || ModHelper.hasRelic(WaxOfPropagation.ID)) {
            inner2.addOption(OPTIONS[5], (i) -> {
                RelicEventHelper.addReward(rewards -> {
                    RewardItem reward = new RewardItem();
                    RewardEditor.getInstance().setRewardByPath(CustomEnums.PROPAGATION, reward, true);
                    rewards.add(reward);
                    RewardItem reward2 = new RewardItem();
                    RewardEditor.getInstance().setRewardByPath(CustomEnums.PRESERVATION, reward2, true);
                    rewards.add(reward2);
                });
                transitionKey(Phase.PRESERVE);
            });
        }
        /*if (ModHelper.hasRelic(WaxOfDestruction.ID)) {
            
        }*/
        registerPhase(Phase.INNER2, inner2);
        registerPhase(Phase.EMBRACE, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[7], (i) -> openMap()));
        registerPhase(Phase.WAIT, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[7], (i) -> openMap()));
        registerPhase(Phase.PRESERVE, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[7], (i) -> openMap()));
        
        transitionKey(Phase.START);
    }
    
    public enum Phase {
        START,
        LEAVE,
        INNER1,
        INNER2,
        EMBRACE,
        WAIT,
        PRESERVE,
        ENTER,
    }
}
