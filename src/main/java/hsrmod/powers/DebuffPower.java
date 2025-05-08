package hsrmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class DebuffPower extends BasePower{
    public DebuffPower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, PowerType.DEBUFF, upgraded);
    }
    
    public DebuffPower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, PowerType.DEBUFF, false);
    }
    
    public DebuffPower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, PowerType.DEBUFF, upgraded);
    }
    
    public DebuffPower(String id, AbstractCreature owner){
        super(id, owner, 0, PowerType.DEBUFF, false);
    }
}
