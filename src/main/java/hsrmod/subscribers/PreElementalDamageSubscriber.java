package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;

public interface PreElementalDamageSubscriber extends ISubscriber {
    float preElementalDamage(ElementalDamageAction action);
}
