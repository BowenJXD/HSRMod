package hsrmod.minion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MinionPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "<class>"
    )
    public static class AddFields {
        public static SpireField<MinionGroup> playerMinions = new SpireField(() -> new MinionGroup());

        public AddFields() {
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "showIntent"
    )
    public static class showIntentPatch {
        public showIntentPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).showIntent();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "init"
    )
    public static class InitPatch {
        public InitPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).init();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "usePreBattleAction"
    )
    public static class UsePreBattleActionPatch {
        public UsePreBattleActionPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).usePreBattleAction();
        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class ApplyPreTurnLogicPatch {
        public ApplyPreTurnLogicPatch() {
        }

        @SpireInsertPatch(
                rloc = 64
        )
        public static void Insert(AbstractRoom _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).applyPreTurnLogic();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "update"
    )
    public static class MonsterGroupUpdatePatch {
        public MonsterGroupUpdatePatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).update();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "updateAnimations"
    )
    public static class UpdateAnimationsPatch {
        public UpdateAnimationsPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).updateAnimations();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "render"
    )
    public static class MonsterGroupRenderPatch {
        public MonsterGroupRenderPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance, SpriteBatch sb) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).render(sb);
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "applyEndOfTurnPowers"
    )
    public static class ApplyEndOfTurnPowersPatch {
        public ApplyEndOfTurnPowersPatch() {
        }

        @SpirePostfixPatch
        public static void Postfix(MonsterGroup _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).applyEndOfTurnPowers();
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "renderReticle"
    )
    public static class RenderReticlePatch {
        public RenderReticlePatch() {
        }

        @SpirePostfixPatch
        public static void Insert(MonsterGroup _instance, SpriteBatch sb) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).renderReticle(sb);
        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "endTurn"
    )
    public static class EndTurnPatch {
        public EndTurnPatch() {
        }

        @SpireInsertPatch(
                rloc = 1
        )
        public static SpireReturn<Void> Insert(AbstractRoom _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).applyEndOfTurnTriggers();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class PlayerOnVictoryPatch {
        public PlayerOnVictoryPatch() {
        }

        @SpirePostfixPatch
        public static void Insert(AbstractPlayer _instance) {
            ((MinionGroup)AddFields.playerMinions.get(AbstractDungeon.player)).onVictory();
        }
    }

    @SpirePatch(
            clz = DamageAction.class,
            method = "<ctor>",
            paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class}
    )
    public static class ChangeDamageActionTargetPatch {
        public ChangeDamageActionTargetPatch() {
        }

        @SpirePostfixPatch
        public static SpireReturn<Void> Postfix(DamageAction _instance, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
            if (target != null && target.isPlayer && !(info.owner instanceof AbstractPlayer)) {
                /*if (info.owner instanceof AbstractMonster && (info.owner.hasPower(DecreePower.POWER_ID) || info.owner.hasPower(MentalCollapsePower.POWER_ID))) {
                    _instance.target = info.owner;
                    if (info.owner.hasPower(MentalCollapsePower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(info.owner, info.owner, MentalCollapsePower.POWER_ID));
                    }
                } else if (AbstractDungeon.player.hasPower(ChaoticLogicPower.POWER_ID)) {
                    _instance.target = info.owner;
                } else*/ if (/*!AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) &&*/ !MinionGroup.areMinionsBasicallyDead()) {
                    AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                    if (minion != null) {
                        _instance.target = minion;
                    }
                }

               /* if (info.owner instanceof AbstractMonster && info.owner.hasPower(IllusionPower.POWER_ID)) {
                    DamageInfo i = new DamageInfo(info.owner, info.base, info.type);
                    float mul = (float)info.owner.getPower(IllusionPower.POWER_ID).amount * 0.1F * (float)info.output;
                    if (AbstractDungeon.player instanceof KosakaShiori && SkillTreeOpenButton.getSkillButton(2).point > 0) {
                        mul *= 1.0F + (float)SkillTreeOpenButton.getSkillButton(2).getEffect() * 0.01F;
                    }

                    i.output = (int)mul;
                    if (i.output < 1) {
                        i.output = 1;
                    }

                    AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, i, effect));
                }*/
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = VampireDamageAction.class,
            method = "<ctor>",
            paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class}
    )
    public static class ChangeVampireDamageActionTargetPatch {
        public ChangeVampireDamageActionTargetPatch() {
        }

        @SpirePostfixPatch
        public static SpireReturn<Void> Postfix(VampireDamageAction _instance, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
            if (target != null && target.isPlayer && !(info.owner instanceof AbstractPlayer)) {
                /*if (info.owner instanceof AbstractMonster && (info.owner.hasPower(DecreePower.POWER_ID) || info.owner.hasPower(MentalCollapsePower.POWER_ID))) {
                    _instance.target = info.owner;
                    if (info.owner.hasPower(MentalCollapsePower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(info.owner, info.owner, MentalCollapsePower.POWER_ID));
                    }
                } else if (AbstractDungeon.player.hasPower(ChaoticLogicPower.POWER_ID)) {
                    _instance.target = info.owner;
                } else*/ if (/*!AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) &&*/ !MinionGroup.areMinionsBasicallyDead()) {
                    AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                    if (minion != null) {
                        _instance.target = minion;
                    }
                }

                /*if (info.owner instanceof AbstractMonster && info.owner.hasPower(IllusionPower.POWER_ID)) {
                    DamageInfo i = new DamageInfo(info.owner, info.base, info.type);
                    float mul = (float)info.owner.getPower(IllusionPower.POWER_ID).amount * 0.1F * (float)info.output;
                    if (AbstractDungeon.player instanceof KosakaShiori && SkillTreeOpenButton.getSkillButton(2).point > 0) {
                        mul *= 1.0F + (float)SkillTreeOpenButton.getSkillButton(2).getEffect() * 0.01F;
                    }

                    i.output = (int)mul;
                    if (i.output < 1) {
                        i.output = 1;
                    }

                    AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, i, effect));
                }*/
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class ChangePlayerDamageTargetPatch {
        public ChangePlayerDamageTargetPatch() {
        }

        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Void> Insert(AbstractPlayer _instance, DamageInfo info) {
            if (!(info.owner instanceof AbstractPlayer)) {
                /*if (info.owner instanceof AbstractMonster && (info.owner.hasPower(DecreePower.POWER_ID) || info.owner.hasPower(MentalCollapsePower.POWER_ID))) {
                    info.owner.damage(info);
                    if (info.owner.hasPower(MentalCollapsePower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(info.owner, info.owner, MentalCollapsePower.POWER_ID));
                    }

                    return SpireReturn.Return();
                }

                if (AbstractDungeon.player.hasPower(ChaoticLogicPower.POWER_ID)) {
                    info.owner.damage(info);
                    return SpireReturn.Return();
                }
*/
                if (/*!AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) &&*/ !MinionGroup.areMinionsBasicallyDead()) {
                    AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                    if (minion != null) {
                        minion.damage(info);
                        return SpireReturn.Return();
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "calculateDamage"
    )
    public static class ChangeMonsterCalculateTargetPatch {
        public ChangeMonsterCalculateTargetPatch() {
        }

        @SpireInsertPatch(
                rloc = 20,
                localvars = {"tmp"}
        )
        public static SpireReturn<Void> Insert(AbstractMonster _instance, int damage, @ByRef float[] tmp) {
            if (/*((!_instance.hasPower(DecreePower.POWER_ID) || !AbstractDungeon.player.hasPower(ChaoticLogicPower.POWER_ID) || !AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) || !_instance.hasPower(MentalCollapsePower.POWER_ID)) &&*/ 
                    !MinionGroup.areMinionsBasicallyDead()) {
                AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                if (minion != null) {
                    for(AbstractPower p : minion.powers) {
                        tmp[0] = p.atDamageReceive(tmp[0], DamageInfo.DamageType.NORMAL);
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "calculateDamage"
    )
    public static class ChangeMonsterCalculateTargetPatch2 {
        public ChangeMonsterCalculateTargetPatch2() {
        }

        @SpireInsertPatch(
                rloc = 38,
                localvars = {"tmp"}
        )
        public static SpireReturn<Void> Insert(AbstractMonster _instance, int damage, @ByRef float[] tmp) {
            if (/*(!_instance.hasPower(DecreePower.POWER_ID) || !_instance.hasPower(ChaoticLogicPower.POWER_ID) || !AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) || !_instance.hasPower(MentalCollapsePower.POWER_ID)) && */
                    !MinionGroup.areMinionsBasicallyDead()) {
                AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                if (minion != null) {
                    for(AbstractPower p : minion.powers) {
                        tmp[0] = p.atDamageFinalReceive(tmp[0], DamageInfo.DamageType.NORMAL);
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = DamageInfo.class,
            method = "applyPowers"
    )
    public static class ChangeMonsterCalculateTargetPatch4 {
        public ChangeMonsterCalculateTargetPatch4() {
        }

        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Void> Insert(DamageInfo info, AbstractCreature owner, @ByRef AbstractCreature[] _target) {
            if (_target[0] != null && _target[0].isPlayer && !(info.owner instanceof AbstractPlayer)) {
                if (!(info.owner instanceof AbstractMonster)/* || !info.owner.hasPower(DecreePower.POWER_ID) && !info.owner.hasPower(MentalCollapsePower.POWER_ID)*/) {
                    /*if (AbstractDungeon.player.hasPower(ChaoticLogicPower.POWER_ID)) {
                        _target[0] = info.owner;
                    } else*/ if (/*!AbstractDungeon.player.hasPower(CoverMinionPower.POWER_ID) &&*/ !MinionGroup.areMinionsBasicallyDead() && !owner.isPlayer && !(owner instanceof AbstractPlayerMinion)) {
                        AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
                        if (minion != null) {
                            _target[0] = minion;
                        }
                    }
                } else {
                    _target[0] = info.owner;
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "onModifyPower"
    )
    public static class OnApplyPowerPatch {
        public OnApplyPowerPatch() {
        }

        @SpirePostfixPatch
        public static void Postfix() {
            if (!MinionGroup.areMinionsBasicallyDead()) {
                for(AbstractPlayerMinion m : MinionGroup.getMinions()) {
                    m.applyPowers();
                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {boolean.class}
    )
    public static class OnMonsterDeathPatch {
        public OnMonsterDeathPatch() {
        }

        @SpireInsertPatch(
                rloc = 16
        )
        public static SpireReturn<Void> Insert(AbstractMonster _instance, boolean triggerRelic) {
            for(AbstractPlayerMinion minion : MinionGroup.getMinions()) {
                if (minion.getTargetMonster() != null && minion.getTargetMonster().isDeadOrEscaped()) {
                    minion.onMonsterDeath();
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SpawnMonsterAction.class,
            method = "update"
    )
    public static class SpawnMonsterActionPatch {
        public SpawnMonsterActionPatch() {
        }

        @SpirePostfixPatch
        public static SpireReturn<Void> Postfix(SpawnMonsterAction _instance) {
            if (_instance.isDone) {
                for(AbstractPlayerMinion minion : MinionGroup.getMinions()) {
                    if (minion.getTargetMonster() != null && minion.getTargetMonster().isDeadOrEscaped()) {
                        minion.onSpawnMonster();
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }
}
