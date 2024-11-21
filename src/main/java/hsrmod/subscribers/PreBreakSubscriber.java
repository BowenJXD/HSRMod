package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementalDamageInfo;

public interface PreBreakSubscriber extends IHSRSubscriber{
    void preBreak(ElementalDamageInfo info, AbstractCreature target);
}
