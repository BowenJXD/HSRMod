package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;

public class SombrousSepulcher extends BaseMonster {
    public static final String ID = SombrousSepulcher.class.getSimpleName();
    
    public SombrousSepulcher(int health, float x, float y) {
        super(ID, 100F, 200F, x, y);
        setHp(health);
    }

    @Override
    public void takeTurn() {
        AbstractPower power = new IntangiblePower(this, 1);
        power.name = MOVES[0];
        addToBot(new ApplyPowerAction(this, this, power, 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.NONE);
    }
    
    public void update() {
        super.update();
        this.animY = -MathUtils.cosDeg((float)(System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    public void addBlock(int blockAmount) {
        super.addBlock(0);
    }
}
