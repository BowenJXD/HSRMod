package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.enemyOnly.MultiMovePower;
import hsrmod.utils.ModHelper;

public class WoodenLupus extends BaseMonster {
    public static final String ID = WoodenLupus.class.getSimpleName();
    
    int shockCount = 1;
    
    public WoodenLupus(float x, float y) {
        super(ID, 150, 219, x, y);
        
        shockCount = specialAs ? 2 : 1;
        
        addSlot(x - 150, y + 300 + AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(x + 150, y + 300 + AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = slot -> {
            ShadowJackhyena m = new ShadowJackhyena(slot.x, slot.y);
            m.currentHealth = this.currentHealth;
            m.healthBarUpdatedEvent();
            return m;
        };
        
        addMove(Intent.ATTACK_DEBUFF, moreDamageAs ? 3 : 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new ShockPower(p, this, shockCount)));
        });
        addMove(Intent.UNKNOWN, mi -> {
            spawnMonsters(1, true);
        });
    }
    
    @Override
    protected void getMove(int i) {
        int emptyCount = getEmptySlotCount();
        if (i < emptyCount * 50 && !lastMove((byte) 1)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
