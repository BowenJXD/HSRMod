package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class CorrosionPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(CorrosionPower.class.getSimpleName());

    int healAmt = 1;
    int vigorAmt = 1;
    
    public CorrosionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], healAmt, vigorAmt);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0) {
            flash();
            addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1));
            remove(1);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new VigorPower(AbstractDungeon.player, 1), 1));
    }
}
