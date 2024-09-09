package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.green.Catalyst;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.uniqueBuffs.SlashedDreamPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class TheDoubtfulFourfoldRoot extends BaseCard {
    public static final String ID = TheDoubtfulFourfoldRoot.class.getSimpleName();
    
    public TheDoubtfulFourfoldRoot() {
        super(ID);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Iterator<AbstractMonster> mons = AbstractDungeon.getMonsters().monsters.iterator();
        
        while (mons.hasNext()) {
            Iterator<AbstractPower> var = mons.next().powers.iterator();

            while(var.hasNext()) {
                AbstractPower power = var.next();
                if ((upgraded && power.type == AbstractPower.PowerType.DEBUFF)
                        || (!upgraded && power instanceof DoTPower))
                    power.stackPower(1);
            }
        }
    }
}
