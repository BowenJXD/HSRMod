package hsrmod.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.CustomEnums;

public abstract class StatePower extends BasePower{
    public StatePower(String id, AbstractCreature owner, int Amount, boolean upgraded){
        super(id, owner, Amount, NeutralPowertypePatch.NEUTRAL, upgraded);
    }
    
    public StatePower(String id, AbstractCreature owner, int Amount){
        super(id, owner, Amount, NeutralPowertypePatch.NEUTRAL, false);
    }
    
    public StatePower(String id, AbstractCreature owner, boolean upgraded){
        super(id, owner, 0, NeutralPowertypePatch.NEUTRAL, upgraded);
    }
    
    public StatePower(String id, AbstractCreature owner){
        super(id, owner, 0, NeutralPowertypePatch.NEUTRAL, false);
    }
}
