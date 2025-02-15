package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.UnlockToughnessAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.time.Duration;

public class SwordFormationPower extends StatePower implements PostMonsterDeathSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SwordFormationPower.class.getSimpleName());
    
    int damageIncrement = 50;
    int damageDecrement = 50;
    int hpLoss = 20;
    
    public SwordFormationPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        amount = -1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], damageIncrement, damageDecrement, hpLoss);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        addToBot(new UnlockToughnessAction(owner, owner));
        int hpLossAmount = owner.currentHealth * hpLoss / 100;
        int tr = ModHelper.getPowerCount(owner, ToughnessPower.POWER_ID);
        addToBot(new ElementalDamageAction(owner, new ElementalDamageInfo(owner, hpLossAmount, DamageInfo.DamageType.HP_LOSS, ElementType.None, tr), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        return damageAmount * (100 + damageIncrement) / 100;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        return damageAmount * (100 - damageDecrement) / 100;
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this) && AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> m.hasPower(FormationCorePower.POWER_ID) && m != monster)) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }
}
