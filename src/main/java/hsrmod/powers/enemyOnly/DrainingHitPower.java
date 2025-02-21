package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class DrainingHitPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(DrainingHitPower.class.getSimpleName());

    public DrainingHitPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new HealAction(owner, owner, amount));
        }
    }
}
