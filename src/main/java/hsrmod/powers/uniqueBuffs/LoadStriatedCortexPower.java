package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class LoadStriatedCortexPower extends PowerPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(LoadStriatedCortexPower.class.getSimpleName());
    
    int[] damageIncrement;
    
    public LoadStriatedCortexPower(int[] damageIncrement) {
        super(POWER_ID);
        this.damageIncrement = damageIncrement;
        updateDescription();
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        if (!(o instanceof AbstractCard)) return false;
        for (AbstractDamageModifier mod : list) {
            if (mod instanceof LoadStriatedCortexModifier) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new LoadStriatedCortexModifier(damageIncrement));
    }
    
    public static class LoadStriatedCortexModifier extends AbstractDamageModifier {
        public int[] damageIncrement;

        public LoadStriatedCortexModifier(int[] damageIncrement) {
            super();
            this.damageIncrement = damageIncrement;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            if ((card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL)) {
                int enemyCount = 0;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    int i = m.isDeadOrEscaped() ? 0 : 1;
                    enemyCount += i;
                }
                int increment = 0;
                if (enemyCount <= damageIncrement.length && enemyCount > 0) {
                    increment = damageIncrement[enemyCount - 1];
                }
                increment = Math.max(0, increment);
                return damage + increment;
            }
            return damage;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new LoadStriatedCortexModifier(damageIncrement);
        }
    }
}
