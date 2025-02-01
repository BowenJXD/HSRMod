package hsrmod.subscribers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface PostMonsterDeathSubscriber extends IHSRSubscriber {
    void postMonsterDeath(AbstractMonster monster);
}
