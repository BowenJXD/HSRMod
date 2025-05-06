package androidTestMod.powers;

import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class StatePower extends BasePower{
    public StatePower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, CustomEnums.STATUS, upgraded);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, CustomEnums.STATUS, false);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, CustomEnums.STATUS, upgraded);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner){
        super(id, owner, 0, CustomEnums.STATUS, false);
        priority = 4;
    }
}
