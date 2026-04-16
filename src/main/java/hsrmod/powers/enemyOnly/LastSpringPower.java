package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class LastSpringPower extends BuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(LastSpringPower.class.getSimpleName());
    
    public LastSpringPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                ModHelper.addToBotAbstract(() -> {
                    if (ModHelper.check(m) && m.currentHealth < m.maxHealth) 
                        addToTop(new HealAction(m, owner, amount));
                });
            }
        }
        return damageAmount;
    }


    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}
