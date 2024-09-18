package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.subscribers.SubscribeManager;

import java.util.Iterator;

public class BreakDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;

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
            
            float tmp = this.info.output;
            Iterator var1 = this.target.powers.iterator();
            while (var1.hasNext()) {
                AbstractPower p = (AbstractPower) var1.next();
                tmp = p.atDamageReceive(this.info.output, this.info.type);
            }
            
            this.info.output = (int) SubscribeManager.getInstance().triggerPreBreakDamage(tmp, this.target);
            //
            
            addToTop(new DamageAction(this.target, this.info));

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
