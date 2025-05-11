package hsrmod.powers.misc;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ReduceToughnessAction;
import hsrmod.misc.IHSRCharacter;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.BuffPower;

public class ToughnessPower extends BuffPower implements InvisiblePower{
    public static final String POWER_ID = HSRMod.makePath(ToughnessPower.class.getSimpleName());

    int stackLimit = 0;

    public ToughnessPower(AbstractCreature owner, int Amount, int stackLimit) {
        super(POWER_ID, owner, Amount);
        this.priority = 20;
        this.stackLimit = stackLimit;
        this.amount = Math.min(Math.max(-stackLimit, Amount), stackLimit);
        this.canGoNegative = true;

        this.updateDescription();
    }

    public ToughnessPower(AbstractCreature owner, int Amount) {
        this(owner, Amount, getStackLimit(owner));
    }

    public ToughnessPower(AbstractCreature owner) {
        this(owner, getStackLimit(owner));
    }

    @Override
    public void updateDescription() {
        if (amount > 0)
            this.description = String.format(DESCRIPTIONS[0], this.amount, stackLimit);
        else
            this.description = String.format(DESCRIPTIONS[1], -this.amount, stackLimit);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        if (amount <= 0 && !owner.hasPower(LockToughnessPower.POWER_ID) && !owner.isPlayer) {
            alterPower(stackLimit * 2);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage * (1 - (this.amount / 100.0F));
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!(info instanceof ElementalDamageInfo)
                && !(AbstractDungeon.player instanceof IHSRCharacter)
                && info.type != DamageInfo.DamageType.HP_LOSS) {
            addToTop(new ReduceToughnessAction(owner, info.owner, 2, ElementType.None));
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount == stackLimit && owner != AbstractDungeon.player && BaseMod.hasModID("spireTogether:")) return;
        alterPower(stackAmount);
    }

    public void alterPower(int i) {
        if (owner.hasPower(LockToughnessPower.POWER_ID)) return;
        this.fontScale = 8.0F;
        this.amount += i;

        if (this.amount < -stackLimit) {
            this.amount = -stackLimit;
        } else if (this.amount > stackLimit) {
            this.amount = stackLimit;
        }

        // addToTop(new TalkAction(owner, String.format("Amount before: %d, Amount: %d, stack limit: %d!", temp, stackAmount, stackLimit), 1.0F, 2.0F));
        type = this.amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }

    public static int getStackLimit(AbstractCreature c) {
        int result = 0;
        if (c.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower power = (ToughnessPower) c.getPower(ToughnessPower.POWER_ID);
            if (power != null) result = power.stackLimit;
        } else if (c instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) c;
            switch (m.type) {
                case NORMAL:
                    result = AbstractDungeon.ascensionLevel < 7 ? 4 : 5;
                    break;
                case ELITE:
                    result = AbstractDungeon.ascensionLevel < 8 ? 6 : 7;
                    break;
                case BOSS:
                    result = AbstractDungeon.ascensionLevel < 9 ? 10 : 12;
                    break;
            }
            result *= Math.min(AbstractDungeon.actNum, 4);
            if (BaseMod.hasModID("spireTogether:")) {
                result += result / 2;
            }
            int count = HSRModConfig.getActiveTPCount();
            if (count > 0) {
                result += (int) (result * HSRModConfig.getTVInc());
            }
        } else if (c instanceof AbstractPlayer) {
            result = 50;
        }
        return result;
    }
}
