package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;

public class SummonedPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(SummonedPower.class.getSimpleName());
    
    public SummonedPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        amount = -1;
        this.updateDescription();
    }
    
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onDeath() {
        super.onDeath();
        AbstractCreature boss = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> m.type == AbstractMonster.EnemyType.BOSS && !(m.isDying || m.isEscaping || m.halfDead || m.currentHealth <= 0))
                .findFirst().orElse(null);
        if (boss != null) {
            addToTop(new ElementalDamageAction(boss, new ElementalDamageInfo(boss, owner.maxHealth, DamageInfo.DamageType.HP_LOSS, null, ToughnessPower.getStackLimit(owner)), AbstractGameAction.AttackEffect.NONE));
        }
    }
}
