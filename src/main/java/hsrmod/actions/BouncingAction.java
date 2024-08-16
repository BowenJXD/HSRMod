package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import hsrmod.powers.WindShearPower;

import java.util.Objects;

public class BouncingAction extends AbstractGameAction {
    private static final float DURATION = 0.01F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private int numTimes;
    private int amount;
    private String powerName;
    private ElementalDamageAction damageAction;

    public BouncingAction(AbstractCreature target, int amount, int numTimes, String powerName, ElementalDamageAction damageAction) {
        this.target = target;
        this.actionType = ActionType.DEBUFF;
        this.duration = 0.01F;
        this.numTimes = numTimes;
        this.amount = amount;
        this.powerName = powerName;
        this.damageAction = damageAction;
    }

    public void update() {
        if (this.target == null) {
            this.isDone = true;
        } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else {
            if (this.numTimes > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                --this.numTimes;
                AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
                this.addToTop(new BouncingAction(randomMonster, this.amount, this.numTimes, powerName, this.damageAction));
                this.addToTop(new VFXAction(new PotionBounceEffect(this.target.hb.cX, this.target.hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
            }

            if (this.target.currentHealth > 0) {
                AbstractPower power = this.getPower(powerName, this.target, AbstractDungeon.player, this.amount);
                if (power != null) 
                    this.addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, power, this.amount, true, AttackEffect.POISON));
                ElementalDamageAction damageAction = this.damageAction.makeCopy();
                damageAction.target = this.target;
                this.addToTop(damageAction);
                this.addToTop(new WaitAction(0.1F));
            }

            this.isDone = true;
        }
    }
    
    public AbstractPower getPower(String powerName, AbstractCreature target, AbstractCreature source, int amount) {
        AbstractPower result = null;
        if (Objects.equals(powerName, WindShearPower.POWER_ID)) {
            result = new WindShearPower(target, source, amount);
        }
        else if (Objects.equals(powerName, PoisonPower.POWER_ID)) {
            result = new PoisonPower(target, source, amount);
        }
        return result;
    }
}
