package hsrmod.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnPlayerDamagedSubscriber extends IHSRSubscriber {
    int receiveOnPlayerDamaged(int var1, DamageInfo var2);
}
