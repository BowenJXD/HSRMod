package hsrmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class PowerPower extends BasePower implements NonStackablePower {
    public PowerPower(String id, int Amount, boolean upgraded){
        super(id, AbstractDungeon.player, Amount, PowerType.BUFF, upgraded);
    }
    
    public PowerPower(String id, int Amount){
        super(id, AbstractDungeon.player, Amount, PowerType.BUFF, false);
    }
    
    public PowerPower(String id, boolean upgraded){
        super(id, AbstractDungeon.player, 0, PowerType.BUFF, upgraded);
    }
    
    public PowerPower(String id){
        super(id, AbstractDungeon.player, 0, PowerType.BUFF, false);
    }
}
