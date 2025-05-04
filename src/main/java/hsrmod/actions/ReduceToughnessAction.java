package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;

import java.util.Iterator;

/**
 * Used for other characters to reduce toughness of hsr enemies.
 */
public class ReduceToughnessAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    public int tr;
    private ElementType elementType;

    public ReduceToughnessAction(AbstractCreature target, AbstractCreature source, int tr, ElementType elementType) {
        setValues(target, source, tr);
        this.target = target;
        this.tr = tr;
        this.duration = DURATION;
        this.elementType = elementType;
        
        String playerName = AbstractDungeon.player.chosenClass.name();
        if (playerName.equals("Aventurine_CLASS")) {
            this.tr = MathUtils.random(1, 6);
        } else if (playerName.equals("ACHERON_CHAR")) {
            this.tr = target.hasPower("AcheronMod:Flow") ? 3 : 2;
        }
    }

    public void update() {
        if ((this.shouldCancelAction()) || this.target == null) {
            this.isDone = true;
            return;
        }
        // Start of the action
        if (this.duration == 0.1F) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));
        }

        if (Settings.FAST_MODE) this.isDone = true;
        else this.tickDuration();
        if (!this.isDone) return;

        this.target.tint.changeColor(Color.WHITE.cpy());

        // Reduce toughness
        ToughnessPower toughnessPower = target.hasPower(ToughnessPower.POWER_ID) ? (ToughnessPower) this.target.getPower(ToughnessPower.POWER_ID) : null;
        tr = (int) SubscriptionManager.getInstance().triggerPreToughnessReduce(tr, this.target, elementType);
        
        // check break
        boolean didBreak = false;
        if (target != null
                && toughnessPower != null
                && toughnessPower.amount > 0
                && toughnessPower.amount <= tr
                && !target.hasPower(LockToughnessPower.POWER_ID)) {
            didBreak = true;
        }

        // break logic
        if (didBreak && !target.isDeadOrEscaped()) {
            // trigger PreBreak
            SubscriptionManager.getInstance().triggerPreBreak(new ElementalDamageInfo(source, 0, elementType, tr), target);
            // broken
            addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BrokenPower(target, 1), 1));
        }

        // reduce toughness
        if (toughnessPower != null) {
            addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ToughnessPower(target, -tr)));
        }

        // Check to remove actions except HealAction, GainBlockAction, UseCardAction, TriggerCallbackAction, and DamageAction
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator<AbstractGameAction> i = AbstractDungeon.actionManager.actions.iterator();

            while (i.hasNext()) {
                AbstractGameAction e = (AbstractGameAction) i.next();
                if (!(e instanceof HealAction)
                        && !(e instanceof GainBlockAction)
                        && !(e instanceof UseCardAction)
                        && !(e instanceof ElementalDamageAction.TriggerCallbackAction)
                        && e.actionType != ActionType.DAMAGE) {
                    i.remove();
                }
            }
        }
        //
    }
}
