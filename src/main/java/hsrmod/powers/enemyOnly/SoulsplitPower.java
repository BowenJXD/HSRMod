package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class SoulsplitPower extends StatePower implements PostMonsterDeathSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SoulsplitPower.class.getSimpleName());
    
    public SoulsplitPower(AbstractMonster owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        ToughnessPower toughness = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
        if (toughness != null) {
            toughness.lock(this);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        ToughnessPower toughness = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
        if (toughness != null) {
            toughness.unlock(this);
        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS)
            return 0;
        return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS)
            return 0;
        return damageAmount;
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this) && monster != owner) {
            addToBot(new LoseHPAction(owner, owner, amount));
        }
    }
}
