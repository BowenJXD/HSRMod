package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BouncingAction extends AbstractGameAction {
    private int numTimes;
    private ElementalDamageAction damageAction;

    public BouncingAction(AbstractCreature target, int numTimes, ElementalDamageAction damageAction){
        this.target = target;
        this.actionType = ActionType.DEBUFF;
        this.duration = 0.01F;
        this.numTimes = numTimes;
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
                if (randomMonster != null) {
                    this.addToTop(new BouncingAction(randomMonster, this.numTimes, this.damageAction.makeCopy()));
                    // this.addToTop(new VFXAction(new PotionBounceEffect(this.target.hb.cX, this.target.hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
                }
            }

            if (this.target.currentHealth > 0) {
                ElementalDamageAction damageAction = this.damageAction;
                damageAction.target = this.target;
                this.addToTop(new WaitAction(0.1F));
                this.addToTop(damageAction);
            }

            this.isDone = true;
        }
    }
}
