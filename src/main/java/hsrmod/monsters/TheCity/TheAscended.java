package hsrmod.monsters.TheCity;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.enemyOnly.PranaSiphonedPower;
import hsrmod.utils.ModHelper;

public class TheAscended extends BaseMonster {
    public static final String ID = TheAscended.class.getSimpleName();

    int windShearReq = 5;
    
    public TheAscended(float x, float y) {
        super(ID, 340, 384, x, y);
        
        windShearReq = specialAs ? 3 : 5;

        addMove(Intent.ATTACK_DEBUFF, 9, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new PranaSiphonedPower(p)));
        });
        addMove(Intent.ATTACK_DEBUFF, 3, ModHelper.moreDamageAscension(type) ? 4 : 5, mi -> {
            for (int i = 0; i < mi.damageTimeSupplier.get(); i++) {
                addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, dmg -> {
                    if (dmg > 0) {
                        addToTop(new ApplyPowerAction(p, this, new WindShearPower(p, this, 1)));
                    }
                }));
            }
            if (specialAs) {
                ModHelper.addToBotAbstract(() -> {
                    if (p.hasPower(WindShearPower.POWER_ID)) {
                        addToTop(new ApplyPowerAction(p, this, new PranaSiphonedPower(p)));
                    }
                });
            }
        });
        addMoveA(Intent.ATTACK_BUFF, 15, mi -> {
            shout(0, 1);
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AttackAnim.JUMP);
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
        });
        
        floatIndex = 1;
    }

    @Override
    public void update() {
        super.update();
        
    }

    @Override
    protected void getMove(int i) {
        if (ModHelper.getPowerCount(p, WindShearPower.POWER_ID) >= windShearReq && !lastMove((byte) 2)) {
            setMove(2);
        } else if (!p.hasPower(PranaSiphonedPower.POWER_ID)) {
            setMove(0);
        } else {
            setMove(1);
        }
    }
}
