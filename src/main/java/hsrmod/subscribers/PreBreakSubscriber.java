package hsrmod.subscribers;

import hsrmod.modcore.ElementalDamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBreakSubscriber extends IHSRSubscriber{
    void preBreak(ElementalDamageInfo info, AbstractCreature target);
}
