package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;

import java.util.function.Consumer;

public class ElementalDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private ElementType elementType;
    private int toughnessReduction;
    private Consumer<AbstractCreature> afterEffect;

    public ElementalDamageAction(AbstractCreature target, DamageInfo info, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> afterEffect) {
        this.elementType = elementType;
        this.toughnessReduction = toughnessReduction;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.afterEffect = afterEffect;
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
                        
            this.target.damage(this.info);

            addToBot(new ReduceToughnessAction(target, this.toughnessReduction, this.elementType, this.info));
            
            if (this.afterEffect != null) {
                this.afterEffect.accept(this.target);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
