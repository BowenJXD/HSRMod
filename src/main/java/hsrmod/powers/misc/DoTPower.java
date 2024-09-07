package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;

import java.util.Iterator;

public abstract class DoTPower extends AbstractPower {
    private AbstractCreature source;
    
    public boolean removeOnTrigger = true;

    public DoTPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
        
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    public void atStartOfTurn() {
        trigger();
    }
    
    public void trigger(){
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            
            float dmg = this.getDamage();
            Iterator var1 = owner.powers.iterator();
            
            while(var1.hasNext()) {
                AbstractPower p = (AbstractPower)var1.next();
                dmg = p.atDamageReceive(dmg, DamageInfo.DamageType.NORMAL);
            }            
            
            this.addToBot(new ElementalDamageAction(this.owner, new DamageInfo(this.source, (int) dmg), this.getElementType(), 1));
            if (removeOnTrigger) remove();
        }
    }
    
    public abstract int getDamage();
    public abstract ElementType getElementType();
    
    public void remove() {
        if (this.amount <= 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
