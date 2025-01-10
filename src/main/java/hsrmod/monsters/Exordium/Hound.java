package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class Hound extends BaseMonster {
    public static final String ID = Hound.class.getSimpleName();
    
    int heal = 6;
    
    public Hound(float x, float y) {
        super(ID, 0F, -15.0F, 179, 139, x, y);
        setDamagesWithAscension(6);
        heal = ModHelper.specialAscension(type) ? 7 : 6;
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            case 1:
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (ModHelper.checkMonster(monster)) {
                        addToBot(new HealAction(monster, this, heal));
                    }
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.currentHealth < m.maxHealth / 2 && ModHelper.checkMonster(m))) {
            setMove((byte) 1, Intent.BUFF);
        } else {
            setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
        }
        turnCount++;
    }
}
