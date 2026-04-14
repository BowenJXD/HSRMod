package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

import java.util.Objects;

public class ObsessionPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ObsessionPower.class.getSimpleName());

    static final int STACK_LIMIT = 8;
    
    public ObsessionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], STACK_LIMIT, amount, amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > STACK_LIMIT) {
            amount = STACK_LIMIT;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        addToBot(new AddTemporaryHPAction(owner, owner, amount));
    }
    
    @SpirePatch(clz = AbstractOrb.class, method = "applyFocus")
    @SpirePatch(clz = Dark.class, method = "applyFocus")
    public static class ObsessionPatch {
        public static void Postfix(AbstractOrb orb) {
            AbstractPower power = AbstractDungeon.player.getPower(ObsessionPower.POWER_ID);
            if (power == null) return;
            if (Objects.equals(orb.ID, Dark.ORB_ID)) {
                orb.passiveAmount += Math.max(0, power.amount);
            }
            else if (!orb.ID.equals(Plasma.ORB_ID)) {
                orb.passiveAmount += Math.max(0, power.amount);
                orb.evokeAmount += Math.max(0, power.amount);
            }
        }
    }
}
