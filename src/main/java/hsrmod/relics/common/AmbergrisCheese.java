package hsrmod.relics.common;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import hsrmod.relics.BaseRelic;

import java.util.Iterator;

public class AmbergrisCheese extends BaseRelic {
    public static final String ID = AmbergrisCheese.class.getSimpleName();
    
    public AmbergrisCheese() {
        super(ID);
    }

    @Override
    public void onVictory() {
        flash();
        addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }
}
