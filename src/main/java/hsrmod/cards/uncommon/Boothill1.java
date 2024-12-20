package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Boothill1 extends BaseCard {
    public static final String ID = Boothill1.class.getSimpleName();
    
    int costCache = -1;
    
    public Boothill1() {
        super(ID);
        costCache = cost;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new ElementalDamageAction(m, new ElementalDamageInfo(this),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                    )
            );
        }
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        ModHelper.addToBotAbstract(() -> {
            if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                    && (toughness > 0)){
                // addToBot(new GainEnergyAction(1));
                int val = ToughnessPower.getStackLimit(m);
                addToBot(new BreakDamageAction(m, new DamageInfo(p, val)));
            }
            else if (EnergyPanel.getCurrentEnergy() > 0) {
                returnToHand = true;
                retain = true;
                setCostForTurn(costCache);
                // addToBot(new LoseEnergyAction(1));
            }
        });
    }
}
