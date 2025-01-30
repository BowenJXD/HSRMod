package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class ReverberationPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(ReverberationPower.class.getSimpleName());

    public ReverberationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (amount > 1) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            if (owner.isPlayer) {
                addToBot(new PressEndTurnButtonAction());
            } else {
                addToBot(new StunMonsterAction((AbstractMonster) owner, owner));
            }
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            addToBot(new ApplyPowerAction(owner, owner, new ReverberationPower(owner, 1)));
        }
        return damageAmount;
    }
}
