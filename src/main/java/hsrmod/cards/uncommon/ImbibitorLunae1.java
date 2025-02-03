package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;

public class ImbibitorLunae1 extends BaseCard {
    public static final String ID = ImbibitorLunae1.class.getSimpleName();
    
    public ImbibitorLunae1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        
        int x = 0;
        switch (energyOnUse) {
            case 0:
                x = 2;
                break;
            case 1:
                x = 3;
                break;
            case 2:
                x = 5;
                break;
            default:
                x = 7;
                break;
        }
        
        for (int i = 0; i < x; i++) {
            addToBot(new ElementalDamageAction(
                    m,
                    new ElementalDamageInfo(this), 
                    AbstractGameAction.AttackEffect.SLASH_VERTICAL
            ));
        }
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, energyOnUse * 20), energyOnUse * 20));
        addToBot(new LoseEnergyAction(energyOnUse));
    }
}
