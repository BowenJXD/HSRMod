package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DeceptionsCrownPower;
import hsrmod.utils.ModHelper;

public class InsatiableVanity extends BaseMonster {
    public static final String ID = InsatiableVanity.class.getSimpleName();
    
    int deceptionsCrownCount = 4;
    
    public InsatiableVanity(float x, float y) {
        super(ID, 200, 200, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;
        
        deceptionsCrownCount = specialAs ? 5 : 4;
        
        addMove(Intent.UNKNOWN, mi->{
            ModHelper.addToBotAbstract(() -> {
                AbstractMonster monster = ModHelper.getRandomMonster(m -> !(m instanceof InsatiableVanity || m.hasPower(DeceptionsCrownPower.POWER_ID)), true);
                if (monster != null)
                    addToBot(new ApplyPowerAction(monster, this, new DeceptionsCrownPower(monster, deceptionsCrownCount)));
            });
            addToBot(new SuicideAction(this));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
