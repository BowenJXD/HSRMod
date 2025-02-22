package hsrmod.actions;

import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class BouncingAction extends AbstractGameAction {
    private int numTimes;
    private ElementalDamageAction damageAction;
    BaseCard card;
    
    public BouncingAction(AbstractCreature target, int numTimes, ElementalDamageAction damageAction, BaseCard card){
        this.target = target;
        this.actionType = ActionType.DEBUFF;
        this.duration = 0.01F;
        this.numTimes = numTimes;
        this.damageAction = damageAction;
        this.card = card;
    }
    
    public BouncingAction(AbstractCreature target, int numTimes, ElementalDamageAction damageAction){
        this(target, numTimes, damageAction, null);
    }
    
    public BouncingAction(int numTimes, ElementalDamageAction damageAction, BaseCard card){
        this(null, numTimes, damageAction, card);
    }
    
    public void update() {
        if (this.target == null || target.currentHealth <= 0) {
            this.target = AbstractDungeon.getRandomMonster();
            if (target == null) {
                this.isDone = true;
                return;
            }
        }
        
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
            return;
        }
        
        if (this.numTimes > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            --this.numTimes;
            this.addToTop(new BouncingAction(this.numTimes, this.damageAction.makeCopy(), card));
        }

        if (this.target.currentHealth > 0) {
            ElementalDamageAction damageAction = this.damageAction;
            damageAction.target = this.target;
            if (card != null) {
                this.card.calculateCardDamage((AbstractMonster) this.target);
                damageAction.info = new ElementalDamageInfo(this.card);
            }
            this.addToTop(damageAction);
        }
        this.isDone = true;
    }
}
