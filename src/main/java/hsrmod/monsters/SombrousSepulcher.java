package hsrmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;

public class SombrousSepulcher extends AbstractMonster {
    public static final String ID = SombrousSepulcher.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    public SombrousSepulcher(int health, float x, float y) {
        super(NAME, HSRMod.makePath(ID), health, 0F, -15.0F, 100F, 200F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void takeTurn() {
        AbstractPower power = new IntangiblePlayerPower(this, 2);
        power.name = MOVES[0];
        addToBot(new ApplyPowerAction(this, this, power, 1));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.NONE);
    }
    
    public void update() {
        super.update();
        this.animY = -MathUtils.cosDeg((float)(System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }
}
