package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ObscureBlazePower;
import hsrmod.powers.enemyOnly.SafeguardPower;
import hsrmod.utils.ModHelper;

public class ShellOfFadedRage extends BaseMonster {
    public static final String ID = ShellOfFadedRage.class.getSimpleName();

    int damageTime = 3;

    public ShellOfFadedRage(float x, float y) {
        super(ID, 300, 384, x, y);

        damageTime = moreDamageAs ? 4 : 3;

        addMoveA(Intent.ATTACK_BUFF, 10, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.LIGHTNING);
            addToBot(new ApplyPowerAction(this, this, new ObscureBlazePower(this, specialAs ? 2 : 1)));
        });
        addMove(Intent.BUFF, mi -> {
            addToBot(new ApplyPowerAction(this, this, new ObscureBlazePower(this, 1)));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        addMove(Intent.ATTACK, 5,
                () -> {
                    return damageTime + ModHelper.getPowerCount(this, ObscureBlazePower.POWER_ID);
                }, mi -> {
                    if (hasPower(ChargingPower.POWER_ID)) {
                        int dmgTime = mi.damageTimeSupplier.get();
                        addToBot(new AnimateSlowAttackAction(this));
                        for (int i = 0; i < dmgTime; i++) {
                            addToBot(new VFXAction(new LightningEffect(p.hb.cX, p.hb.cY), 0.1F));
                            addToBot(new DamageAction(p, this.damage.get(mi.index), AbstractGameAction.AttackEffect.LIGHTNING));
                        }
                    }
                });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SafeguardPower(this)));
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(2);
        } else if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
