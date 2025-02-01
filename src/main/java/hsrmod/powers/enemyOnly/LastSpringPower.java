package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class LastSpringPower extends BuffPower {
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
                addToBot(new HealAction(m, owner, amount));
            }
        }
        return damageAmount;
    }
}
