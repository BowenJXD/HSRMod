package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class RegressionInequalityOfAnnihilation extends BaseCard {
    public static final String ID = RegressionInequalityOfAnnihilation.class.getSimpleName();

    public RegressionInequalityOfAnnihilation() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int playerToughness = p.hasPower(ToughnessPower.POWER_ID) ? p.getPower(ToughnessPower.POWER_ID).amount : 0;
        int totalToughness = playerToughness;
        totalToughness += AbstractDungeon.getMonsters().monsters.stream().
                mapToInt(mo -> ModHelper.getPowerCount(mo, ToughnessPower.POWER_ID)).sum();
        int avgToughness = totalToughness / (AbstractDungeon.getMonsters().monsters.size() + magicNumber);

        int stackNumber = avgToughness * magicNumber - playerToughness;
        addToTop(new ApplyPowerAction(p, p, new ToughnessPower(p, stackNumber), stackNumber));

        AbstractDungeon.getMonsters().monsters.forEach(monster -> {
            int monsterToughness = ModHelper.getPowerCount(monster, ToughnessPower.POWER_ID);
            int stackNum = avgToughness - monsterToughness;
            addToTop(new ApplyPowerAction(monster, monster, new ToughnessPower(monster, stackNum), stackNum));
        });
    }
}
