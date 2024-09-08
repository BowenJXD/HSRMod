package hsrmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class BuffPower extends BasePower{
    public BuffPower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, PowerType.BUFF, upgraded);
    }
    
    public BuffPower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, PowerType.BUFF, false);
    }
    
    public BuffPower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, PowerType.BUFF, upgraded);
    }
    
    public BuffPower(String id, AbstractCreature owner){
        super(id, owner, 0, PowerType.BUFF, false);
    }
}
