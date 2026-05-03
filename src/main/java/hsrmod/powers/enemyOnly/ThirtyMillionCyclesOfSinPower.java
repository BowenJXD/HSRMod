package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class ThirtyMillionCyclesOfSinPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(ThirtyMillionCyclesOfSinPower.class.getSimpleName());

    int[] perfectNums = {6, 28, 496, 8128, 33550336};
    int index = 0;
    
    AbstractCreature target;
    
    public ThirtyMillionCyclesOfSinPower(AbstractCreature owner, int amount, AbstractCreature target) {
        super(POWER_ID, owner, amount);
        this.priority = 0;
        this.target = target;
        updateDescription();
        loadRegion("heartDef");
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        if (target != null) {
            addToBot(new DamageAction(target, new DamageInfo(target, amount, DamageInfo.DamageType.HP_LOSS)));
        }
        ModHelper.addToBotAbstract(() -> addToTop(new ExhaustToHandAction(GeneralUtil.getRandomElement(AbstractDungeon.player.exhaustPile.group, AbstractDungeon.cardRandomRng))));
        stackPower(-perfectNums[Math.min(index++, perfectNums.length-1)] + perfectNums[Math.min(index, perfectNums.length-1)]);
        updateDescription();
    }
}
