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
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        if (i > 0 && damageInfo.type == DamageInfo.DamageType.NORMAL) {
            AbstractCreature target = damageInfo.owner;
            if (target != null && target != lastTarget) {
                attack(target, owner.currentBlock - i);
                lastTarget = target;
            }
            // attack(target, Math.min(i, owner.currentBlock));
        }
        return i;
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
