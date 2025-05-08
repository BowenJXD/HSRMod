package hsrmod.subscribers;

import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.DoTPower;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreDoTDamageSubscriber extends IHSRSubscriber {
    float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power); 
}
