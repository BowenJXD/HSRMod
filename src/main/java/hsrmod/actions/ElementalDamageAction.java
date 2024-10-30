package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.mod.stslib.patches.ColoredDamagePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ElementalDamageAction extends AbstractGameAction {
    public ElementalDamageInfo info;
    private Consumer<AbstractCreature> callback;
    private Function<AbstractCreature, Integer> modifier;
    public boolean doApplyPower = false;
    public boolean isFast = false;

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info,
                                 AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> callback, Function<AbstractCreature, Integer> modifier) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.callback = callback;
        this.modifier = modifier;
        ColoredDamagePatch.DamageActionColorField.damageColor.set(this, info.elementType.getColor());
        ColoredDamagePatch.DamageActionColorField.fadeSpeed.set(this, ColoredDamagePatch.FadeSpeed.SLOW);
    }

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info, AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> callback) {
        this(target, info, effect, callback, null);
    }

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info, AbstractGameAction.AttackEffect effect) {
        this(target, info, effect, null);
    }

    public ElementalDamageAction setIsFast(boolean isFast) {
        this.isFast = isFast;
        return this;
    }

    public ElementalDamageAction setCallback(Consumer<AbstractCreature> callback) {
        this.callback = callback;
        return this;
    }

    public ElementalDamageAction setModifier(Function<AbstractCreature, Integer> modifier) {
        this.modifier = modifier;
        return this;
    }

    public void update() {
        if ((this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS)
                || this.target == null) {
            this.isDone = true;
            return;
        }
        if (this.duration == 0.1F) {
            if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
        }

        if (this.isFast) this.isDone = true;
        else this.tickDuration();

        if (!this.isDone) return;

        this.target.tint.color.set(info.elementType.getColor());
        this.target.tint.changeColor(Color.WHITE.cpy());

        if (this.doApplyPower) {
            this.applyPowers();
        }
        if (this.modifier != null) {
            this.info.output += this.modifier.apply(this.target);
        }

        // Reduce toughness
        AbstractPower toughnessPower = this.target.getPower(ToughnessPower.POWER_ID);
        info.tr = (int) SubscriptionManager.getInstance().triggerPreToughnessReduce(info.tr, this.target, info.elementType);

        // Apply damage
        this.target.damage(this.info);
        if (callback != null) addToTop(new TriggerCallbackAction(this.callback, this.target));
        //addToTop(new ReduceToughnessAction(target, info.tr, info.elementType, this.info));
        //

        if (target != null && !target.isDeadOrEscaped()) {
            if (toughnessPower != null && toughnessPower.amount > 0 && toughnessPower.amount <= info.tr) {
                int breakDamage = info.elementType.getBreakDamage();
                addToBot(new BreakDamageAction(target, new DamageInfo(info.owner, breakDamage)));
                addToBot(info.applyBreakingPower(target));
                addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BrokenPower(target, 1), 1));
            }
            if (toughnessPower != null) {
                addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ToughnessPower(target, -info.tr), -info.tr));
            }
        }
        //

        // Check to remove actions except HealAction, GainBlockAction, UseCardAction, TriggerCallbackAction, and DamageAction
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator<AbstractGameAction> i = AbstractDungeon.actionManager.actions.iterator();

            while (i.hasNext()) {
                AbstractGameAction e = (AbstractGameAction) i.next();
                if (!(e instanceof HealAction)
                        && !(e instanceof GainBlockAction)
                        && !(e instanceof UseCardAction)
                        && !(e instanceof TriggerCallbackAction)
                        && e.actionType != ActionType.DAMAGE) {
                    i.remove();
                }
            }
        }
        //
    }

    public static class TriggerCallbackAction extends AbstractGameAction {
        private Consumer<AbstractCreature> callback;
        private AbstractCreature target;

        public TriggerCallbackAction(Consumer<AbstractCreature> callback, AbstractCreature target) {
            this.callback = callback;
            this.target = target;
        }

        public void update() {
            if (this.callback != null) this.callback.accept(this.target);
            this.isDone = true;
        }
    }

    public ElementalDamageAction applyPowers() {
        this.info.applyPowers(this.info.owner, this.target);
        return this;
    }

    public ElementalDamageAction makeCopy() {
        ElementalDamageAction result = new ElementalDamageAction(this.target, this.info, this.attackEffect, this.callback, this.modifier).setIsFast(this.isFast);
        result.doApplyPower = this.doApplyPower;
        return result;
    }
}
