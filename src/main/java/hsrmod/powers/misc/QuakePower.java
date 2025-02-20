package hsrmod.powers.misc;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import basemod.interfaces.PreMonsterTurnSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cardsV2.Preservation.Quake;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;

public class QuakePower extends BuffPower implements PreMonsterTurnSubscriber, OnLoseBlockPower {
    public static final String POWER_ID = HSRMod.makePath(QuakePower.class.getSimpleName());

    AbstractCreature lastTarget;
    
    public QuakePower(AbstractCreature creature, int amount) {
        super(POWER_ID, creature, amount);
        this.updateDescription();
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
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        if (i > 0 
                && damageInfo.type == DamageInfo.DamageType.NORMAL) {
            flash();
            remove(1);
            AbstractCreature target = damageInfo.owner;
            if (target != null && target != lastTarget) {
                attack(target);
                lastTarget = target;
            }
            // attack(target, Math.min(i, owner.currentBlock));
        }
        return i;
    }
    
    public void attack(AbstractCreature target) {
        attack(target, 1, null);
    }

    public void attack(AbstractCreature target, float dmgMultiplier, Consumer<ElementalDamageAction.CallbackInfo> callback) {
        addToBot(new ElementalDamageAction(target, new ElementalDamageInfo(owner, (int) (owner.currentBlock * dmgMultiplier), DamageInfo.DamageType.THORNS, ElementType.Physical, 5), AbstractGameAction.AttackEffect.BLUNT_HEAVY).setCallback(callback));
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        if (SubscriptionManager.checkSubscriber(this)) {
            lastTarget = null;
        }
        return true;
    }
}
