package hsrmod.events;

import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.special.ThalanToxiFlame;
import hsrmod.relics.special.ThePinkestCollision;
import hsrmod.relics.starter.PomPomBlessing;
import hsrmod.relics.starter.WaxOfElation;
import hsrmod.relics.starter.WaxOfNihility;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;
import hsrmod.utils.RewardEditor;

public class YuQingtuEvent extends BaseEvent {
    public static final String ID = YuQingtuEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    Ingredient ingredient = Ingredient.SUGAR;
    
    int hpLoss = 20;
    int goldLoss = 20;

    public YuQingtuEvent() {
        super(ID);

        registerPhase(Phase.START, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.WHAT_TO_ADD)));
        registerPhase(Phase.WHAT_TO_ADD, new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> {
                    ingredient = Ingredient.SUGAR;
                    transitionKey(Phase.STIR);
                })
                .addOption(OPTIONS[2], (i) -> {
                    ingredient = Ingredient.TOOTHPASTE;
                    transitionKey(Phase.STIR);
                }));
        
        TextPhase stirPhase = new TextPhase(DESCRIPTIONS[2]);
        if (ModHelper.hasRelic(WaxOfElation.ID)) {
            stirPhase.addOption(OPTIONS[3], (i) -> {
                int r = AbstractDungeon.eventRng.random(99);
                if (r < 70) {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThePinkestCollision.ID), HSRMod.makePath(ThalanToxiFlame.ID));
                    transitionKey(Phase.SUCCESS);
                } else {
                    transitionKey(Phase.EXPLOSION_1);
                }
            });
        } else if (ModHelper.hasRelic(WaxOfNihility.ID)) {
            stirPhase.addOption(OPTIONS[4], (i) -> {
                int r = AbstractDungeon.eventRng.random(99);
                if (r < 50) {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThePinkestCollision.ID));
                } else {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThalanToxiFlame.ID));
                }
                transitionKey(Phase.SUCCESS);
            });
        }
        stirPhase.addOption(OPTIONS[5], (i) -> {
            int r = AbstractDungeon.eventRng.random(99);
            if (ingredient == Ingredient.SUGAR) {
                // sugar and stir gently
                if (r < 50) {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThePinkestCollision.ID));
                    transitionKey(Phase.SUCCESS);
                } else {
                    transitionKey(Phase.THIEF);
                }
            } else {
                // toothpaste and stir gently
                if (r < 20) {
                    transitionKey(Phase.PROVOKE_1);
                } else if (r < 20 + 30) {
                    transitionKey(Phase.EXPLOSION_1);
                } else {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThalanToxiFlame.ID));
                    transitionKey(Phase.SUCCESS);
                }
            }
        });
        stirPhase.addOption(OPTIONS[6], (i) -> {
            int r = AbstractDungeon.eventRng.random(99);
            if (ingredient == Ingredient.SUGAR) {
                // sugar and stir brute
                if (r < 10) {
                    transitionKey(Phase.THIEF);
                } else if (r < 10 + 20) {
                    transitionKey(Phase.EXPLOSION_1);
                } else {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThePinkestCollision.ID));
                    transitionKey(Phase.SUCCESS);
                }
            } else {
                // toothpaste and stir brute
                if (r < 20) {
                    transitionKey(Phase.PROVOKE_1);
                } else {
                    RelicEventHelper.gainRelics(HSRMod.makePath(ThePinkestCollision.ID));
                    transitionKey(Phase.SUCCESS);
                }
            }
        });
        
        registerPhase(Phase.STIR, stirPhase);
        
        registerPhase(Phase.SUCCESS, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[10], (i) -> openMap()));
        
        registerPhase(Phase.EXPLOSION_1, new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[0], (i) -> {
            int dmg = AbstractDungeon.player.currentHealth * hpLoss / 100;
            AbstractDungeon.player.damage(new DamageInfo(null, dmg, DamageInfo.DamageType.HP_LOSS));
            transitionKey(Phase.EXPLOSION_2);
        }));
        
        registerPhase(Phase.EXPLOSION_2, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[10], (i) -> openMap()));

        registerPhase(Phase.THIEF, new TextPhase(DESCRIPTIONS[6])
                .addOption(OPTIONS[7], (i) -> {
                    int gold = AbstractDungeon.player.gold * goldLoss / 100;
                    AbstractDungeon.player.loseGold(gold);
                    transitionKey(Phase.SLEEP);
                })
                .addOption(OPTIONS[8], (i) -> {
                    AbstractRelic relic = AbstractDungeon.player.getRelic(PomPomBlessing.ID);
                    if (relic != null) relic.setCounter(0);
                    transitionKey(Phase.THIEF_BATTLE);
                })
        );
        
        registerPhase(Phase.SLEEP, new TextPhase(DESCRIPTIONS[7]).addOption(OPTIONS[10], (i) -> openMap()));
        
        registerPhase(Phase.THIEF_BATTLE, new CombatPhase(Encounter.VAGRANTS)
                .setNextKey(Phase.BATTLE_WON)
                .addRewards(true, null));
        
        registerPhase(Phase.PROVOKE_1, new TextPhase(DESCRIPTIONS[8]).addOption(OPTIONS[0], (i) -> transitionKey(Phase.PROVOKE_2)));
        
        registerPhase(Phase.PROVOKE_2, new TextPhase(DESCRIPTIONS[9]).addOption(OPTIONS[9], (i) -> transitionKey(Phase.PROVOKE_BATTLE)));
        
        registerPhase(Phase.PROVOKE_BATTLE, new CombatPhase(Encounter.GUARDIAN_SHADOW)
                .setNextKey(Phase.BATTLE_WON)
                .addRewards(true, room -> {
                    for (RewardItem r : room.rewards) {
                        if (r.type == RewardItem.RewardType.CARD) {
                            RewardEditor.setRewardCards(r, AbstractCard.CardRarity.RARE);
                        }
                    }
                }));
        
        registerPhase(Phase.BATTLE_WON, new TextPhase(DESCRIPTIONS[10]).addOption(OPTIONS[10], (i) -> openMap()));

        transitionKey(Phase.START);
    }

    public static enum Ingredient {
        SUGAR,
        TOOTHPASTE,
    }

    public static enum Phase {
        START,
        WHAT_TO_ADD,
        STIR,
        SUCCESS,
        EXPLOSION_1,
        EXPLOSION_2,
        SLEEP,
        THIEF,
        THIEF_BATTLE,
        PROVOKE_1,
        PROVOKE_2,
        PROVOKE_BATTLE,
        BATTLE_WON,
    }
}
