package hsrmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.mod.stslib.patches.ColoredDamagePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;
import hsrmod.subscribers.SubscriptionManager;

import java.util.function.Consumer;
import java.util.function.Function;

public class ElementalDamageAction extends AbstractGameAction{
    private DamageInfo info;
    private ElementType elementType;
    public int toughnessReduction;
    private Consumer<AbstractCreature> afterEffect;
    private Function<AbstractCreature, Integer> modifier;
    public boolean doApplyPower = false;
    public float critRate = 0;
    public float critDamage = 100;

    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, 
                                 AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> afterEffect, Function<AbstractCreature, Integer> modifier) {
        this.elementType = elementType;
        this.toughnessReduction = toughnessReduction;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.afterEffect = afterEffect;
        this.modifier = modifier;
    }
    
    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, 
                                 AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> afterEffect) {
        this(target, info, elementType, toughnessReduction, effect, afterEffect, null);
    }

    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect) {
        this(target, info, elementType, toughnessReduction, effect, null);
    }
    
    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction) {
        this(target, info, elementType, toughnessReduction, AttackEffect.NONE, null);
    }
    
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            
            if (this.doApplyPower) {
                this.applyPowers();
            }
            if (this.modifier != null) {
                this.info.output += this.modifier.apply(this.target);
            }
            SubscriptionManager.getInstance().triggerSetCritRate(this);
            if (critRate > 0 && AbstractDungeon.cardRandomRng.random(99) < critRate) {
                this.info.output *= (int) (1 + (critDamage / 100));
            }
            AbstractGameAction action = new DamageCallbackAction(target, info, this.attackEffect, (dmg) -> {
                if (this.afterEffect != null) {
                    this.afterEffect.accept(this.target);
                }
            });
            ColoredDamagePatch.DamageActionColorField.damageColor.set(action, elementType.getColor());
            ColoredDamagePatch.DamageActionColorField.fadeSpeed.set(action, ColoredDamagePatch.FadeSpeed.SLOW);
            addToTop(action);
            addToTop(new ReduceToughnessAction(target, this.toughnessReduction, this.elementType, this.info));

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
    
    public ElementalDamageAction applyPowers() {
        this.info.applyPowers(this.info.owner, this.target);
        return this;
    }
    
    public ElementalDamageAction makeCopy() {
        ElementalDamageAction result = new ElementalDamageAction(this.target, new DamageInfo(info.owner, info.base, info.type), this.elementType, 
                this.toughnessReduction, this.attackEffect, this.afterEffect, this.modifier);
        result.doApplyPower = this.doApplyPower;
        return result;
    }
}
