package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class EntanglePower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(EntanglePower.class.getSimpleName());

    AbstractCreature source;
    int damage = 3;
    private final int stackLimit = 5;

    public EntanglePower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(POWER_ID, owner, Amount);
        this.source = source;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], stackLimit, damage, getDamage());
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            amount = stackLimit;
        }
        updateDescription();
    }

    int getDamage(){
        return amount * damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL)
            stackPower(1);
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        ElementalDamageAction action = new ElementalDamageAction(owner, new ElementalDamageInfo(source, getDamage(), ElementType.Quantum, 1), AbstractGameAction.AttackEffect.SLASH_HEAVY);
        if (owner.isPlayer) {
            action.setIsSourceNullable(true)
                    .setIsFast(true)
                    .update();
        } else {
            addToBot(action);
        }
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
