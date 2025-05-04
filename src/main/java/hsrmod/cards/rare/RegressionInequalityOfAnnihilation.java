package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class RegressionInequalityOfAnnihilation extends BaseCard {
    public static final String ID = RegressionInequalityOfAnnihilation.class.getSimpleName();
    
    public RegressionInequalityOfAnnihilation() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int playerToughness = p.hasPower(ToughnessPower.POWER_ID) ? p.getPower(ToughnessPower.POWER_ID).amount : 0;
        int totalToughness = playerToughness;
        int sum = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            int powerCount = ModHelper.getPowerCount(mo, ToughnessPower.POWER_ID);
            sum += powerCount;
        }
        totalToughness += sum;
        int avgToughness = totalToughness / (AbstractDungeon.getMonsters().monsters.size() + magicNumber);
        
        int stackNumber = avgToughness * magicNumber - playerToughness;
        if (stackNumber > 0)
            addToTop(new ApplyPowerAction(p, p, new ToughnessPower(p, stackNumber), stackNumber));
        else if (stackNumber < 0)
            addToTop(new ReducePowerAction(p, p, new ToughnessPower(p, -stackNumber), -stackNumber));

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            int monsterToughness = ModHelper.getPowerCount(monster, ToughnessPower.POWER_ID);
            int stackNum = avgToughness - monsterToughness;
            if (stackNum > 0)
                RegressionInequalityOfAnnihilation.this.addToTop(new ApplyPowerAction(monster, monster, new ToughnessPower(monster, stackNum), stackNum));
            else if (stackNum < 0)
                RegressionInequalityOfAnnihilation.this.addToTop(new ReducePowerAction(monster, monster, new ToughnessPower(monster, -stackNum), -stackNum));
        }
    }
}
