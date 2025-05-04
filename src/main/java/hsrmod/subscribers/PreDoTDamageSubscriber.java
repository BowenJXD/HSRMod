package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.DoTPower;

public interface PreDoTDamageSubscriber extends IHSRSubscriber {
    float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power); 
}
