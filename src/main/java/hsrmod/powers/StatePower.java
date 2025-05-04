package hsrmod.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class StatePower extends BasePower{
    public StatePower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, NeutralPowertypePatch.NEUTRAL, upgraded);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, NeutralPowertypePatch.NEUTRAL, false);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, NeutralPowertypePatch.NEUTRAL, upgraded);
        priority = 4;
    }
    
    public StatePower(String id, AbstractCreature owner){
        super(id, owner, 0, NeutralPowertypePatch.NEUTRAL, false);
        priority = 4;
    }
}
