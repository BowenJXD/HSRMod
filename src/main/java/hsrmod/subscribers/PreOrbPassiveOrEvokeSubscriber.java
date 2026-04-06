package hsrmod.subscribers;

import com.megacrit.cardcrawl.orbs.AbstractOrb;

public interface PreOrbPassiveOrEvokeSubscriber extends IHSRSubscriber {
    default int preOrbPassive(AbstractOrb orb, int amountThisTime) {
        return amountThisTime;
    }
    default int preOrbEvoke(AbstractOrb orb, int amountThisTime) {
        return amountThisTime;
    }
}
