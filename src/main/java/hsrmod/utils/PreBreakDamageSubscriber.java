package hsrmod.utils;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBreakDamageSubscriber extends ISubscriber {
    float preBreakDamage(float amount, AbstractCreature target);
}
