package hsrmod.powers.misc;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cardsV2.Preservation.Quake;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

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
            attack(target, Math.min(i, owner.currentBlock));
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
        if (target == null) target = ModHelper.betterGetRandomMonster();
        if (target != null && damage > 0) {
            Quake quake = new Quake();
            quake.baseDamage = damage;
            quake.upgrade();
            addToTop(new MakeTempCardInHandAction(quake));
        }
    }
}
