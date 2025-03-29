package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Consumer;

public class BreakDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    public float multiplier = 1.0F;
    private Consumer<AbstractCreature> callback;
    
    public BreakDamageAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
    }

    public BreakDamageAction(AbstractCreature target, DamageInfo info) {
        this(target, info, AttackEffect.BLUNT_HEAVY);
    }
    
    public BreakDamageAction(AbstractCreature target, DamageInfo info, float multiplier) {
        this(target, info, AttackEffect.BLUNT_HEAVY);
        this.multiplier = multiplier;
    }
    
    public BreakDamageAction setCallback(Consumer<AbstractCreature> callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            
            //
            AbstractPower breakEffect = info.owner.getPower(BreakEffectPower.POWER_ID);
            if (breakEffect != null) {
                breakEffect.flash();
                this.info.output += breakEffect.amount;
            }
            
            this.info.output = (int) (SubscriptionManager.getInstance().triggerPreBreakDamage(this.info.output, this.target) * this.multiplier);

            ModHelper.applyEnemyPowersOnly(this.info, this.target, false);
            //
            
            addToTop(new DamageAction(this.target, this.info));
            
            if (this.callback != null) {
                this.callback.accept(this.target);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
