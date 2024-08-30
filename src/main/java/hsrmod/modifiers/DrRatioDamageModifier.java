package hsrmod.modifiers;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DrRatioDamageModifier extends AbstractDamageModifier {

    public DrRatioDamageModifier() {}
    
    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new DrRatioDamageModifier();
    }
}
