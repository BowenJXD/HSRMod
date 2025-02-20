package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.StatePower;
import hsrmod.powers.breaks.BurnPower;

public class BoilPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(BoilPower.class.getSimpleName());
    
    int dmg = 30, tr = 10;
    
    public BoilPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], dmg, tr);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        addToTop(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 1, !owner.isPlayer)));
        if (owner instanceof AbstractMonster) {
            dmg = ((AbstractMonster) owner).getIntentDmg();
        }
        addToTop(new ElementalDamageAction(owner, new ElementalDamageInfo(owner, dmg, ElementType.Fire, tr), AbstractGameAction.AttackEffect.FIRE));
        if (owner instanceof AbstractMonster) {
            addToTop(new StunMonsterAction((AbstractMonster) owner, owner));
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, true));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            remove(1);
        }
        return damageAmount;
    }
}
