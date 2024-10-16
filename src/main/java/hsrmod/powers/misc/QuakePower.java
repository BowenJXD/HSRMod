package hsrmod.powers.misc;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cardsV2.Quake;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.SubscriptionManager;

import java.lang.annotation.Target;

public class QuakePower extends BuffPower implements OnPlayerLoseBlockSubscriber, OnLoseBlockPower {
    public static final String POWER_ID = HSRMod.makePath(QuakePower.class.getSimpleName());

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
        if (i >= Math.round(owner.currentBlock * 0.5f)) {
            flash();
            remove(1);
            AbstractCreature target = null;
            if (damageInfo != null && damageInfo.owner != null) {
                target = damageInfo.owner;
            }
            i = Math.min(i, owner.currentBlock);
            attack(target, i);
        }
        return i;
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (SubscriptionManager.checkSubscriber(this)
                && i >= Math.round(owner.currentBlock * 0.5f)) {
            flash();
            remove(1);
            attack(null, i);
        }
        return i;
    }

    public void attack(AbstractCreature target, int damage) {
        if (target == null) target = AbstractDungeon.getRandomMonster();
        if (target != null && damage > 0) {
            Quake quake = new Quake();
            quake.baseDamage = damage;
            addToTop(new MakeTempCardInHandAction(quake));
        }
    }
}
