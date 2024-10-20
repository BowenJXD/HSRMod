package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.mod.stslib.patches.ColoredDamagePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ElementalDamageAction extends AbstractGameAction{
    public DamageInfo info;
    private ElementType elementType;
    public int toughnessReduction;
    private Consumer<AbstractCreature> callback;
    private Function<AbstractCreature, Integer> modifier;
    public boolean doApplyPower = false;
    public boolean isFast = false;

    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction,
                                 AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> callback, Function<AbstractCreature, Integer> modifier) {
        this.elementType = elementType;
        this.toughnessReduction = toughnessReduction;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.callback = callback;
        this.modifier = modifier;
        ColoredDamagePatch.DamageActionColorField.damageColor.set(this, elementType.getColor());
        ColoredDamagePatch.DamageActionColorField.fadeSpeed.set(this, ColoredDamagePatch.FadeSpeed.SLOW);
    }
    
    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, 
                                 AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> callback) {
        this(target, info, elementType, toughnessReduction, effect, callback, null);
    }

    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect) {
        this(target, info, elementType, toughnessReduction, effect, null);
    }
    
    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction) {
        this(target, info, elementType, toughnessReduction, AttackEffect.NONE, null);
    }
    
    public ElementalDamageAction setIsFast(boolean isFast) {
        this.isFast = isFast;
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
        
        this.target.tint.color.set(elementType.getColor());
        this.target.tint.changeColor(Color.WHITE.cpy());
        
        if (this.doApplyPower) {
            this.applyPowers();
        }
        if (this.modifier != null) {
            this.info.output += this.modifier.apply(this.target);
        }
        
        this.target.damage(this.info);
        addToTop(new TriggerCallbackAction(this.callback, this.target));
        addToTop(new ReduceToughnessAction(target, this.toughnessReduction, this.elementType, this.info));

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator<AbstractGameAction> i = AbstractDungeon.actionManager.actions.iterator();

            while(i.hasNext()) {
                AbstractGameAction e = (AbstractGameAction)i.next();
                if (!(e instanceof HealAction) 
                        && !(e instanceof GainBlockAction) 
                        && !(e instanceof UseCardAction)
                        && !(e instanceof TriggerCallbackAction)
                        && e.actionType != ActionType.DAMAGE) {
                    i.remove();
                }
            }
        }
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
        ElementalDamageAction result = new ElementalDamageAction(this.target, new DamageInfo(info.owner, info.base, info.type), this.elementType, 
                this.toughnessReduction, this.attackEffect, this.callback, this.modifier).setIsFast(this.isFast);
        result.doApplyPower = this.doApplyPower;
        return result;
    }
}
