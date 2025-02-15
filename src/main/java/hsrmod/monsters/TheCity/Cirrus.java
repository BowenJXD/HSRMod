package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.MultiMovePower;
import hsrmod.powers.enemyOnly.SoulsplitPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Cirrus extends BaseMonster {
    public static final String ID = Cirrus.class.getSimpleName();
    
    int soulSplitCount = 10;
    
    public Cirrus() {
        super(ID, 400, 410, -100, 300);
        type = EnemyType.ELITE;
        floatIndex = AbstractDungeon.monsterRng.randomBoolean() ? -1 : 1;
        bgm = "Dancing Fantasms";
        dialogX = -120.0F * Settings.scale;
        dialogY = -50.0F * Settings.scale;

        addSlot(-400, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(-100, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(200, AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = this::getMonster;
        
        addMove(Intent.UNKNOWN, mi -> {
            shoutIf(0, 33);
            spawnMonsters();
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SoulsplitPower(this, soulSplitCount)));
        if (specialAs)
            spawnMonsters();
    }

    @Override
    public void die() {
        shout(1);
        super.die();
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
    
    AbstractMonster getMonster(MonsterSlot slot) {
        AbstractMonster result = null;
        float x = slot.x, y = slot.y;
        switch (AbstractDungeon.monsterRng.random(8)) {
            case 0:
                result = new MaraStruckSoldier(x, y);
                break;
            case 1:
                result = new InternalAlchemist(x, y);
                break;
            case 2:
                result = new MaraStruckWarden(x, y);
                break;
            case 3:
                result = new Ballistarius(x, y);
                break;
            case 4:
                result = new IlluminationDragonfish(x, y);
                break;
            case 5:
                result = new ObedientDracolion(x, y);
                break;
            case 6:
                result = new GoldenCloudToad(x, y);
                break;
            case 7:
                result = new WoodenLupus(x, y);
                break;
            case 8:
                result = new GoldenHound(x, y);
                break;
        }

        return result;
    }
}
