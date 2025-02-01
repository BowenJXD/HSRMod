package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.enemyOnly.MultiMovePower;
import hsrmod.utils.ModHelper;

public class GoldenHound extends BaseMonster {
    public static final String ID = GoldenHound.class.getSimpleName();
    
    public GoldenHound(float x, float y) {
        super(ID, 150, 222, x, y);
        
        addMove(Intent.ATTACK, 8, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (ModHelper.check(m) && m != this && m.type == EnemyType.NORMAL) {
                    addToTop(new ApplyPowerAction(m, this, new MultiMovePower(m, 1)));
                }
            }
        })));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
