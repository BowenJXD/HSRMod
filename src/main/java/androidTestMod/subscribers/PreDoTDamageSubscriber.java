package androidTestMod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.misc.DoTPower;

public interface PreDoTDamageSubscriber extends IHSRSubscriber {
    float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power); 
}
