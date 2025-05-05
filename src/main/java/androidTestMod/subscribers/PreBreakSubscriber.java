package androidTestMod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.ElementalDamageInfo;

public interface PreBreakSubscriber extends IHSRSubscriber{
    void preBreak(ElementalDamageInfo info, AbstractCreature target);
}
