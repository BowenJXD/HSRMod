package androidTestMod.subscribers;

import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.misc.DoTPower;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreDoTDamageSubscriber extends IHSRSubscriber {
    float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power); 
}
