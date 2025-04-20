package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class AlienDreamPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(AlienDreamPower.class.getSimpleName());

    boolean justApplied = true;
    int dmgMultiplier = 90;
    int healMultiplier = 10;

    public AlienDreamPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], dmgMultiplier, healMultiplier, dmgMultiplier);
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            addToTop(new HealAction(owner, owner, Math.round(damageAmount * healMultiplier / 100f)));
        }
        return damageAmount * (100 - dmgMultiplier) / 100;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        justApplied = false;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && !justApplied) {
            remove(1);
            return damageAmount * dmgMultiplier / 100;
        }
        return damageAmount;
    }
}
