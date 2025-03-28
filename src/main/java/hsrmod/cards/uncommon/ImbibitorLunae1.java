package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;
import me.antileaf.signature.utils.SignatureHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class ImbibitorLunae1 extends BaseCard {
    public static final String ID = ImbibitorLunae1.class.getSimpleName();
    
    public ImbibitorLunae1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        
        int x = 0;
        switch (energyOnUse + (p.hasRelic("Chemical X") ? 2 : 0)) {
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
        
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < x; i++) {
            addToBot(new ElementalDamageAction(
                    m,
                    new ElementalDamageInfo(this), 
                    AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                    ci -> {
                        if (total.addAndGet(ci.info.output) > 55) {
                            SignatureHelper.unlock(cardID, true);
                        }
                    }
            ));
        }
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, energyOnUse * 20), energyOnUse * 20));
        addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
