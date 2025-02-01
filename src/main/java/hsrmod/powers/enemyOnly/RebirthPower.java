package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.CleanAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.ModHelper;

public class RebirthPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(RebirthPower.class.getSimpleName());

    public RebirthPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        
        updateDescription();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        ModHelper.addToBotAbstract(() -> {
            if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> ModHelper.check(m) && m.hasPower(POWER_ID))) {
                AbstractDungeon.getMonsters().monsters.stream()
                        .filter(m -> m.hasPower(DrainingHitPower.POWER_ID))
                        .forEach(m -> addToTop(new RemoveSpecificPowerAction(m, m, DrainingHitPower.POWER_ID)));
            }
        });
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount >= owner.currentHealth) {
            addToTop(new HealAction(owner, owner, owner.maxHealth / 2));
            addToBot(new CleanAction(owner, owner.powers.size(), true));
            remove(1);
            return owner.currentHealth - 1;
        }
        return damageAmount;
    }
}
