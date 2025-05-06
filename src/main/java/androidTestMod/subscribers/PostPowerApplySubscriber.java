package androidTestMod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface PostPowerApplySubscriber extends IHSRSubscriber {
    void receivePostPowerApplySubscriber(AbstractPower var1, AbstractCreature var2, AbstractCreature var3);
}
