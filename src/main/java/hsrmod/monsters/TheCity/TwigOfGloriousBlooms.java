package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.LastSpringPower;
import hsrmod.utils.ModHelper;

public class TwigOfGloriousBlooms extends BaseMonster {
    public static final String ID = TwigOfGloriousBlooms.class.getSimpleName();
    
    int healAmount = 10;
    int lastSpringCount = 4;
    
    public TwigOfGloriousBlooms(float x, float y) {
        super(ID, 100, 231, x, y);
        
        healAmount = moreHPAs ? 10 : 8;
        lastSpringCount = moreHPAs ? 5 : 4;
        
        addMove(Intent.BUFF, mi -> {
            AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
                addToBot(new HealAction(m, this, healAmount));
            });
            if (specialAs)
                AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof AbundantEbonDeer).forEach(m -> {
                    addToBot(new ApplyPowerAction(m, this, new RegenerateMonsterPower(m, 1)));
                });
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new LastSpringPower(this, lastSpringCount)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
