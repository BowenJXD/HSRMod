package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class Irontomb extends BaseMonster {
    public static final String ID = Irontomb.class.getSimpleName();
    
    public Irontomb() {
        super(ID, 444, 50, 200, -160);
        
        addMove(Intent.NONE, mi -> {});
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new CannotLoseAction());
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }

    @Override
    public void die() {
        super.die();
        addToBot(new CanLoseAction());
        ModHelper.addToBotAbstract(() -> {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                addToTop(new SuicideAction(monster));
            }
        });
    }
}
