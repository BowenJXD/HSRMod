package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.*;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueBuffs.Trailblazer5Power;
import hsrmod.subscribers.SubscribeManager;

public class ReduceToughnessAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    public  int toughnessReduction;
    private ElementType elementType;
    private DamageInfo info;

    public ReduceToughnessAction(AbstractCreature target, int toughnessReduction, ElementType elementType, DamageInfo info) {
        this.target = target;
        this.toughnessReduction = toughnessReduction;
        this.duration = DURATION;
        this.elementType = elementType;
        this.info = info;
        this.source = info.owner;
    }

    public void update() {
        if (this.duration == 0.1F) {
            //
            AbstractPower toughnessPower = this.target.getPower(ToughnessPower.POWER_ID);
            
            if (AbstractDungeon.player.hasPower(BreakEfficiencyPower.POWER_ID))
                toughnessReduction += (int)((float)toughnessReduction 
                        * (float)AbstractDungeon.player.getPower(BreakEfficiencyPower.POWER_ID).amount 
                        * BreakEfficiencyPower.MULTIPLIER);

            toughnessReduction = (int) SubscribeManager.getInstance().triggerPreToughnessReduce(toughnessReduction, this.target, this.elementType);
            
            if (toughnessPower != null 
                    && toughnessPower.amount > 0 
                    && toughnessPower.amount <= toughnessReduction) {
                int breakDamage = elementType.getBreakDamage();
                addToBot(new BreakDamageAction(target, new DamageInfo(info.owner, breakDamage)));
                addToBot(applyBreakingPower());
                addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BrokenPower(target, 1), 1));
            }
            if (toughnessPower != null) {
                addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ToughnessPower(target, -toughnessReduction), -toughnessReduction));
            }

            if (info.owner.hasPower(Trailblazer5Power.POWER_ID)){
                ((Trailblazer5Power) info.owner.getPower(Trailblazer5Power.POWER_ID)).trigger(this);
            }
            //
        }

        this.tickDuration();
    }
    
    ApplyPowerAction applyBreakingPower(){
        AbstractPower power = null;
        int stackNum = 1;
        switch (elementType){
            case Ice:
                power = new FrozenPower(target, stackNum);
                break;
            case Physical:
                power = new BleedingPower(target, AbstractDungeon.player, stackNum);
                break;
            case Fire:
                power = new BurnPower(target, AbstractDungeon.player, stackNum);
                break;
            case Lightning:
                power = new ShockPower(target, AbstractDungeon.player, stackNum);
                break;
            case Wind:
                AbstractMonster monster = (AbstractMonster)target;
                if (monster != null) {
                    switch (monster.type) {
                        case NORMAL:
                            break;
                        case ELITE:
                            stackNum = 2;
                            break;
                        case BOSS:
                            stackNum = 3;
                            break;
                        default:
                            break;
                    }
                }
                power = new WindShearPower(target, AbstractDungeon.player, stackNum);
                break;
            case Quantum:
                power = new EntanglePower(target, source, stackNum);
                break;
            case Imaginary:
                power = new ImprisonPower(target, stackNum);
                break;
            default:
                break;
        }
        if (power != null){
            return new ApplyPowerAction(target, AbstractDungeon.player, power, stackNum);
        }
        return null;
    }
}
