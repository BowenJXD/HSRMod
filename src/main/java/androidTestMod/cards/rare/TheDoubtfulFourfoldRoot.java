package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.misc.DoTPower;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;

import java.util.Iterator;

public class TheDoubtfulFourfoldRoot extends BaseCard {
    public static final String ID = TheDoubtfulFourfoldRoot.class.getSimpleName();
    
    public TheDoubtfulFourfoldRoot() {
        super(ID);
        // exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Iterator<AbstractMonster> mons = AbstractDungeon.getMonsters().monsters.iterator();
        
        while (mons.hasNext()) {
            AbstractMonster mon = mons.next();
            if (!ModHelper.check(mon)) continue;
            addToBot(new VFXAction(new BossCrystalImpactEffect(mon.hb.cX, mon.hb.cY)));
            Iterator<AbstractPower> var = mon.powers.iterator();

            while(var.hasNext()) {
                AbstractPower power = var.next();
                if ((upgraded && power.type == AbstractPower.PowerType.DEBUFF)
                        || (!upgraded && power instanceof DoTPower))
                    if (power.amount > 0) power.stackPower(1);
                    else power.stackPower(-1);
            }
        }
    }
}
