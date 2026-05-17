package hsrmod.monsters.TheEnding;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.modifiers.AntinomyModifier;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.uniqueDebuffs.DestructionFirstPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.cardsV2.Curse.Entangle;
import hsrmod.cardsV2.Curse.Imprison;

import java.util.Objects;

public class Zandar extends BaseMonster implements PostMonsterDeathSubscriber {
    public static final String ID = Zandar.class.getSimpleName();

    int antinomyDmg = moreDamageAs ? 11 : 10;
    int artifactAmt = specialAs ? 3 : 2;
    int evolutionModeAmt = specialAs ? 4 : 3;
    int disputationModeAmt = specialAs ? 8 : 6;
    boolean shouldUseUlt = false;

    public Zandar() {
        super(ID, 512, 512, 0, 80);
        slots.add(new MonsterSlot(-300, -20));
        slots.add(new MonsterSlot(-300, -20));
        slots.add(new MonsterSlot(300, -20));
        
        floatIndex = 1;
        bgm = "NAME == Entelechy";
        monFunc = slot -> {
            switch (slots.indexOf(slot)) {
                case 0: 
                    if (ModHelper.noMonster(FuriaeWarrior.ID))
                        return new FuriaeWarrior(slot.x, slot.y);
                case 1:
                    if (ModHelper.noMonster(TideErodedBlade.ID))
                        return new TideErodedBlade(slot.x, slot.y);
                case 2:
                    if (ModHelper.noMonster(CorrodedAxe.ID))
                        return new CorrodedAxe(slot.x, slot.y);
                default:
                    return null;
            }
        };

        // Move 0: 天球偏移论 — deal 10×2, add 1 Entangle to each of draw/hand/discard
        addMoveA(Intent.ATTACK_DEBUFF, 10, 2, mi -> {
            shout(1, 2, 5.0f);
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
            addToBot(new MakeTempCardInDrawPileAction(new Entangle(), 1, true, true));
            addToBot(new MakeTempCardInHandAction(new Entangle()));
            addToBot(new MakeTempCardInDiscardAction(new Entangle(), 1));
        });

        // Move 1: 潜能实现猜想 — give each summon 5 LastSpring + 1 MultiMove
        addMove(Intent.BUFF, mi -> {
            shout(3, 4, 5.0f);
            for (MonsterSlot slot : slots) {
                if (!slot.isEmpty()) {
                    AbstractMonster summon = slot.monster;
                    addToBot(new ApplyPowerAction(summon, this, new LastSpringPower(summon, 5), 5));
                    addToBot(new ApplyPowerAction(summon, this, new MultiMovePower(summon, 1), 1));
                }
            }
        });

        // Move 2: 智识的二律背反 — deal 35, replace top draw pile card with SolvePrimeCauseConsequence
        addMoveA(Intent.ATTACK_DEBUFF, 35, mi -> {
            shout(5, 6, 5.0f);
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HEAVY, AttackAnim.SLOW);
            applyAntinomy(1);
        });

        // Move 3: 虚数几何演绎法 — repeat 5×: deal 3 damage, random ImprisonPower or Imprison card to discard
        addMoveA(Intent.ATTACK_DEBUFF, 3, 5, mi -> {
            shout(7, 8, 5.0f);
            addToBot(new AnimateSlowAttackAction(this));
            for (int i = 0; i < 5; i++) {
                addToBot(new DamageAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (AbstractDungeon.aiRng.randomBoolean()) {
                    addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1), 1));
                } else {
                    addToBot(new MakeTempCardInDiscardAction(new Imprison(), 1));
                }
            }
        });

        // Move 4: DEL 入侵变量 — deal 10×3, apply 1 ImprisonPower
        addMoveA(Intent.ATTACK_DEBUFF, 10, 3, mi -> {
            shout(11, 12, 5.0f);
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1), 1));
        });

        // Move 5: DEFRAG 生命优化 — each summon loses 10 HP and gains 1 MultiMove
        addMove(Intent.BUFF, mi -> {
            shout(13, 14, 5.0f);
            for (MonsterSlot slot : slots) {
                if (!slot.isEmpty()) {
                    AbstractMonster summon = slot.monster;
                    addToBot(new LoseHPAction(summon, this, 10));
                    addToBot(new ApplyPowerAction(summon, this, new MultiMovePower(summon, 1), 1));
                }
            }
        });

        // Move 6: 创生&&毁灭 — deal 25×2, replace top 2 draw pile cards
        addMoveA(Intent.ATTACK, 25, 2, mi -> {
            shout(15, 16, 5.0f);
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HEAVY, AttackAnim.SLOW);
            applyAntinomy(2);
        });

        // Move 7: FORMAT 故障空间 — repeat 5×: deal 6 damage, random EntanglePower or Entangle card to discard
        addMoveA(Intent.ATTACK_DEBUFF, 6, 5, mi -> {
            shout(17, 18, 5.0f);
            addToBot(new AnimateSlowAttackAction(this));
            for (int i = 0; i < 5; i++) {
                addToBot(new DamageAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (AbstractDungeon.aiRng.randomBoolean()) {
                    addToBot(new ApplyPowerAction(p, this, new EntanglePower(p, this, 1), 1));
                } else {
                    addToBot(new MakeTempCardInDiscardAction(new Entangle(), 1));
                }
            }
        });
        
        addMoveA(Intent.ATTACK, 10, ()-> ModHelper.getPowerCount(this, StrengthenPower.POWER_ID), mi -> {
            shout(19, 7.0f);
            if (mi.damageTimeSupplier != null)
                for (int i = 0; i < mi.damageTimeSupplier.get(); i++) {
                    addToBot(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                    addToBot(new DamageAction(p, this.damage.get(mi.index), AbstractGameAction.AttackEffect.SMASH));
                }
            addToBot(new RemoveSpecificPowerAction(this, this, StrengthenPower.POWER_ID));
            shouldUseUlt = false;
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        shout(0, 5.0f);
        
        SubscriptionManager.subscribe(this);
        addToBot(new ApplyPowerAction(p, this, new DestructionFirstPower(p, 1), 0));
        addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, artifactAmt), 0));
        addToBot(new ApplyPowerAction(this, this, new CorporealFormulaPower(this, 1), 0));
        addToBot(new ApplyPowerAction(this, this, new EvolutionModePower(this, evolutionModeAmt, disputationModeAmt), 0));
        spawnMonsterOnSlot(slots.get(0), SpawnType.MINION, false);
    }

    @Override
    protected void getMove(int i) {
        if (shouldUseUlt) {
            setMove(8);
        } else if (hasPower(DisputationModePower.POWER_ID)) {
            int r;
            do {
                r = AbstractDungeon.aiRng.random(4, 7);
            } while (lastTwoMoves((byte) r));
            setMove(r);
        } else {
            int r;
            do {
                r = AbstractDungeon.aiRng.random(0, 3);
            } while (lastTwoMoves((byte) r));
            setMove(r);
        } 
    }

    @Override
    public void die() {
        super.die();
        shout(20, 5.0f);
    }
    
    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (!SubscriptionManager.checkSubscriber(this) || !ModHelper.check(this)) {
            return;
        }
        if (hasPower(EvolutionModePower.POWER_ID)) {
            ModHelper.addToTopAbstract(() -> {
                if (hasPower(EvolutionModePower.POWER_ID)) {
                    spawnMonsterOnSlot(slots.get(0), SpawnType.MINION, true);
                } else if (!hasPower(DisputationModePower.POWER_ID)) {
                    addToTop(new ApplyPowerAction(this, this, new DisputationModePower(this, disputationModeAmt, evolutionModeAmt), 0));
                    addToBot(new ApplyPowerAction(this, this, new CorporealFormulaPower(this, 1)));

                    for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                        if (Objects.equals(mon.id, HSRMod.makePath(FuriaeWarrior.ID)) && ModHelper.check(mon)) {
                            addToBot(new SuicideAction(mon));
                        }
                    }
                    
                    spawnMonsterOnSlot(slots.get(1), SpawnType.MINION, true);
                    spawnMonsterOnSlot(slots.get(2), SpawnType.MINION, true);

                    if (AbstractDungeon.overlayMenu.endTurnButton.enabled) {
                        addToBot(new PressEndTurnButtonAction());
                        addToBot(new VFXAction(new TimeWarpTurnEndEffect()));
                    }
                }
            });

            addToTop(new ReducePowerAction(this, this, EvolutionModePower.POWER_ID, 1));
        }
        if (hasPower(DisputationModePower.POWER_ID)) {
            ModHelper.addToTopAbstract(() -> {
                if (hasPower(DisputationModePower.POWER_ID)) {
                    if (Objects.equals(monster.id, HSRMod.makePath(TideErodedBlade.ID))) {
                        spawnMonsterOnSlot(slots.get(1), SpawnType.MINION, true);
                    } else if (Objects.equals(monster.id, HSRMod.makePath(CorrodedAxe.ID))) {
                        spawnMonsterOnSlot(slots.get(2), SpawnType.MINION, true);
                    }
                } else if (!hasPower(EvolutionModePower.POWER_ID)) {
                    addToTop(new ApplyPowerAction(this, this, new EvolutionModePower(this, evolutionModeAmt, disputationModeAmt), 0));
                    addToBot(new ApplyPowerAction(this, this, new CorporealFormulaPower(this, 1)));

                    for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                        if (ModHelper.check(mon)) {
                            if (Objects.equals(mon.id, HSRMod.makePath(TideErodedBlade.ID)) || Objects.equals(mon.id, HSRMod.makePath(CorrodedAxe.ID))) {
                                addToBot(new SuicideAction(mon));
                            }
                        }
                    }

                    spawnMonsterOnSlot(slots.get(0), SpawnType.MINION, true);

                    if (AbstractDungeon.overlayMenu.endTurnButton.enabled) addToBot(new PressEndTurnButtonAction());
                    shouldUseUlt = true;
                    addToBot(new RollMoveAction(this));
                    ModHelper.addToBotAbstract(this::createIntent);
                }
            });

            addToTop(new ReducePowerAction(this, this, DisputationModePower.POWER_ID, 1));
        }
    }

    private void applyAntinomy(int count) {
        ModHelper.addToBotAbstract(() -> {
            for (int i = 0; i < Math.min(count, p.drawPile.size()); i++) {
                CardModifierManager.addModifier(AbstractDungeon.player.drawPile.getNCardFromTop(i), new AntinomyModifier(antinomyDmg, GeneralUtil.tryFormat(MOVES[10], antinomyDmg)));
            }
        });
    }
}
