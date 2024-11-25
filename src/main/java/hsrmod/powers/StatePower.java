package hsrmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.CustomEnums;

public abstract class StatePower extends BasePower{
    public StatePower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, CustomEnums.STATUS, upgraded);
    }
    
    public StatePower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, CustomEnums.STATUS, false);
    }
    
    public StatePower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, CustomEnums.STATUS, upgraded);
    }
    
    public StatePower(String id, AbstractCreature owner){
        super(id, owner, 0, CustomEnums.STATUS, false);
    }
}
