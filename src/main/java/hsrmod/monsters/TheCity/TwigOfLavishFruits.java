package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.LavishFruitPower;
import hsrmod.utils.ModHelper;

public class TwigOfLavishFruits extends BaseMonster {
    public static final String ID = TwigOfLavishFruits.class.getSimpleName();
    
    public TwigOfLavishFruits(float x, float y) {
        super(ID, 100, 231, x, y);
        
        addMove(Intent.BUFF, mi -> {
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof AbundantEbonDeer).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new LavishFruitPower(m, 1)));
            });
        });
        addMove(Intent.BUFF, mi -> {
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof AbundantEbonDeer).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new LavishFruitPower(m, 1)));
            });
        });
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> ModHelper.getPowerCount(m, LavishFruitPower.POWER_ID)).sum() <= 0) {
            setMove(0);
        } else {
            setMove(1);
        }
    }
}
