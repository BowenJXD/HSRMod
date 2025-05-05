package androidTestMod.powers.misc;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import basemod.interfaces.PreMonsterTurnSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.modcore.ElementType;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import androidTestMod.subscribers.SubscriptionManager;

import java.util.function.Consumer;

public class QuakePower extends BuffPower implements PreMonsterTurnSubscriber, OnPlayerDamagedSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(QuakePower.class.getSimpleName());

    AbstractCreature lastTarget;
    int tr = 4;
    
    public QuakePower(AbstractCreature creature, int amount) {
        super(POWER_ID, creature, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], tr);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        BaseMod.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this) 
                && i > 0 
                && damageInfo.type == DamageInfo.DamageType.NORMAL
                && owner.currentHealth > 0) {
            AbstractCreature target = damageInfo.owner;
            if (target != null && target != lastTarget) {
                attack(target, owner.currentBlock - i);
                lastTarget = target;
            }
        }
        return 0;
    }
    
    public void attack(AbstractCreature target, int dmg) {
        attack(target, dmg, null);
    }

    public void attack(AbstractCreature target, int dmg, Consumer<ElementalDamageAction.CallbackInfo> callback) {
        flash();
        remove(1);
        addToBot(new ElementalDamageAction(target, new ElementalDamageInfo(owner, dmg, DamageInfo.DamageType.THORNS, ElementType.Physical, tr), AbstractGameAction.AttackEffect.BLUNT_HEAVY).setCallback(callback));
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        if (SubscriptionManager.checkSubscriber(this)) {
            lastTarget = null;
        }
        return true;
    }
}
