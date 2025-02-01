package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.monsters.BaseMonster;

public class GoldenCloudToad extends BaseMonster {
    public static final String ID = GoldenCloudToad.class.getSimpleName();
    
    public GoldenCloudToad(float x, float y) {
        super(ID, 100, 216, x, y);
        floatIndex = AbstractDungeon.monsterRng.randomBoolean() ? -1 : 1;
        
        addMoveA(Intent.ATTACK_DEBUFF, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new DexterityPower(p, -1), -1));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
