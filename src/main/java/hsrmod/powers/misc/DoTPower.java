package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.subscribers.SubscriptionManager;

public abstract class DoTPower extends DebuffPower {
    private AbstractCreature source;
    
    public boolean removeOnTrigger = true;
    
    public int toughnessReduction = 1;

    public DoTPower(String id, AbstractCreature owner, AbstractCreature source, int amount) {
        super(id, owner, amount);
        this.source = source;
        this.isTurnBased = true;
        
        this.updateDescription();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    public void atStartOfTurn() {
        trigger();
    }
    
    public void trigger(){
        trigger(removeOnTrigger);
    }
    
    public void trigger(boolean removeOnTrigger){
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            
            float dmg = this.getDamage();
            ElementalDamageInfo info = new ElementalDamageInfo(this.source, (int) dmg, this.getElementType(), toughnessReduction);
            info.applyPowers(this.source, this.owner);
            
            info.output = (int) SubscriptionManager.getInstance().triggerPreDoTDamage(info.output, this.owner, this);
            
            this.addToTop(new ElementalDamageAction(this.owner, info, AbstractGameAction.AttackEffect.NONE));
            if (removeOnTrigger) remove(1);
        }
    }
    
    public abstract int getDamage();
    public abstract ElementType getElementType();
    
    public static DoTPower getRandomDoTPower(AbstractCreature owner, AbstractCreature source, int amount){
        switch(AbstractDungeon.cardRandomRng.random(0, 3)){
            case 1:
                return new BurnPower(owner, source, amount);
            case 2:
                return new ShockPower(owner, source, amount);
            case 3:
                return new WindShearPower(owner, source, amount);
            case 0:
            default:
                return new BleedingPower(owner, source, amount);
        }
    }

    /**
     * Check if the owner has all the DoT powers (Bleeding, Burn, Shock, WindShear)
     * @param owner
     * @return
     */
    public static boolean hasAllDoTPower(AbstractCreature owner){
        return owner.hasPower(BleedingPower.POWER_ID) 
                && owner.hasPower(BurnPower.POWER_ID) 
                && owner.hasPower(ShockPower.POWER_ID) 
                && owner.hasPower(WindShearPower.POWER_ID);
    }
    
    public static boolean hasAnyDoTPower(AbstractCreature owner){
        return owner.hasPower(BleedingPower.POWER_ID) 
                || owner.hasPower(BurnPower.POWER_ID) 
                || owner.hasPower(ShockPower.POWER_ID) 
                || owner.hasPower(WindShearPower.POWER_ID);
    }
}
