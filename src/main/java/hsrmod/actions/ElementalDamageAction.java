package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Consumer;

public class ElementalDamageAction extends AbstractGameAction {
    public ElementalDamageInfo info;
    private Consumer<CallbackInfo> callback;
    private Consumer<CallbackInfo> modifier;
    public boolean doApplyPower = false;
    public boolean isFast = false;
    public boolean isSourceNullable = false;

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info,
                                 AbstractGameAction.AttackEffect effect, Consumer<CallbackInfo> callback, Consumer<CallbackInfo> modifier) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.callback = callback;
        this.modifier = modifier;
    }

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info, AbstractGameAction.AttackEffect effect, Consumer<CallbackInfo> callback) {
        this(target, info, effect, callback, null);
    }

    public ElementalDamageAction(AbstractCreature target, ElementalDamageInfo info, AbstractGameAction.AttackEffect effect) {
        this(target, info, effect, null);
    }

    public ElementalDamageAction setIsFast(boolean isFast) {
        this.isFast = isFast;
        return this;
    }

    public ElementalDamageAction setCallback(Consumer<CallbackInfo> callback) {
        this.callback = callback;
        return this;
    }

    public ElementalDamageAction setModifier(Consumer<CallbackInfo> modifier) {
        this.modifier = modifier;
        return this;
    }
    
    public ElementalDamageAction setIsSourceNullable(boolean isSourceNullable) {
        this.isSourceNullable = isSourceNullable;
        return this;
    }
    
    public ElementalDamageAction setDoApplyPower(boolean doApplyPower) {
        this.doApplyPower = doApplyPower;
        return this;
    }

    public void update() {
        if ((this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS)
                || this.target == null) {
            this.isDone = true;
            return;
        }
        // Start of the action
        if (this.duration == 0.1F) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));
        }

        if (this.isFast) this.isDone = true;
        else this.tickDuration();

        if (!this.isDone) return;

        this.target.tint.color.set(info.getColor());
        this.target.tint.changeColor(Color.WHITE.cpy());

        if (this.doApplyPower) {
            this.applyPowers();
        }
        if (this.modifier != null) {
            this.modifier.accept(new CallbackInfo(target, false, info));
        }

        // Reduce toughness
        ToughnessPower toughnessPower = target.hasPower(ToughnessPower.POWER_ID) ? (ToughnessPower) this.target.getPower(ToughnessPower.POWER_ID) : null;
        info.tr = (int) SubscriptionManager.getInstance().triggerPreToughnessReduce(info.tr, this.target, info.elementType);
        // Trigger PreElementalDamageAction
        info.output = (int) SubscriptionManager.getInstance().triggerPreElementalDamage(this);

        // Apply damage
        this.target.damage(this.info);

        // check break
        boolean didBreak = false;
        if (target != null
                && toughnessPower != null
                && toughnessPower.amount > 0
                && toughnessPower.amount <= info.tr
                && !target.hasPower(LockToughnessPower.POWER_ID)) {
            didBreak = true;
        }

        // callback
        if (callback != null) {
            addToTop(new TriggerCallbackAction(this.callback, new CallbackInfo(target, didBreak, info)));
        }

        // break logic
        if (didBreak && !target.isDeadOrEscaped()) {
            // trigger PreBreak
            SubscriptionManager.getInstance().triggerPreBreak(info, target);
            // break damage
            int breakDamage = info.getBreakDamage();
            addToBot(new BreakDamageAction(target, new DamageInfo(info.owner, breakDamage)));
            // break power
            ApplyPowerAction action = info.applyBreakingPower(target);
            if (action != null) addToBot(action);
            // broken
            addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BrokenPower(target, 1), 1));
        }

        // reduce toughness
        if (toughnessPower != null && target != null && !target.hasPower(LockToughnessPower.POWER_ID)) {
            addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ToughnessPower(target, -info.tr)));
        }

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

    @Override
    protected boolean shouldCancelAction() {
        if (this.isSourceNullable) return this.target == null || this.target.isDeadOrEscaped();
        return this.target == null || this.source != null && !ModHelper.check(source) || this.target.isDeadOrEscaped();
    }

    public static class TriggerCallbackAction extends AbstractGameAction {
        private Consumer<CallbackInfo> callback;
        private CallbackInfo info;

        public TriggerCallbackAction(Consumer<CallbackInfo> callback, CallbackInfo info) {
            this.callback = callback;
            this.info = info;
        }

        public void update() {
            if (this.callback != null) this.callback.accept(this.info);
            this.isDone = true;
        }
    }

    public static class CallbackInfo {
        public AbstractCreature target;
        public boolean didBreak;
        public ElementalDamageInfo info;

        public CallbackInfo(AbstractCreature target, boolean didBreak, ElementalDamageInfo info) {
            this.target = target;
            this.didBreak = didBreak;
            this.info = info;
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
