package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import java.util.Collections;
import java.util.List;

public class LoadStriatedCortexPower extends PowerPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(LoadStriatedCortexPower.class.getSimpleName());
    
    int damageIncrement = 0;
    
    public LoadStriatedCortexPower(int damageIncrement) {
        super(POWER_ID);
        this.damageIncrement = damageIncrement;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], damageIncrement);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        if (!(o instanceof AbstractCard)) return false;
        if (list.stream().anyMatch(mod -> mod instanceof LoadStriatedCortexModifier)) return false;
        return true;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new LoadStriatedCortexModifier(damageIncrement));
    }
    
    public static class LoadStriatedCortexModifier extends AbstractDamageModifier {
        public int damageIncrement;

        public LoadStriatedCortexModifier(int damageIncrement) {
            super();
            this.damageIncrement = damageIncrement;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            if ((card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL)) {
                int enemyCount = AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> m.isDeadOrEscaped() ? 0 : 1).sum();
                int increment = (4 - enemyCount) * damageIncrement;
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
