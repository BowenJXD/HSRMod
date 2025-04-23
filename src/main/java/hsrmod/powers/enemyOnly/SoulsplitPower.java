package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

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
        addToTop(new ApplyPowerAction(owner, owner, new LockToughnessPower(owner)));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        addToTop(new RemoveSpecificPowerAction(owner, owner, LockToughnessPower.POWER_ID));
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (GameActionManager.turn > 1) {
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> ModHelper.check(m) && m != owner).forEach(m -> {
                addToBot(new ApplyPowerAction(m, owner, new MultiMovePower(m, 1)));
            });
        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS && damage > 1)
            return 1;
        return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 1)
            return 1;
        return damageAmount;
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this) && monster != owner) {
            addToBot(new LoseHPAction(owner, owner, amount));
        }
    }
}
