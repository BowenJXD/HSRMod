package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.enemyOnly.OutragePower;
import hsrmod.powers.enemyOnly.SpurOfThunderwoePower;
import hsrmod.utils.ModHelper;

public class TwigOfWintryWind extends BaseMonster {
    public static final String ID = TwigOfWintryWind.class.getSimpleName();
    
    public TwigOfWintryWind(float x, float y) {
        super(ID, 100, 231, x, y);
        
        addMove(Intent.DEBUFF, mi -> {
            addToBot(new ApplyPowerAction(p, this, new OutragePower(p, 1)));
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, 1)));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
