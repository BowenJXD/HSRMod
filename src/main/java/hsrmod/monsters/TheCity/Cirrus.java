package hsrmod.monsters.TheCity;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.SoulsplitPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class Cirrus extends BaseMonster implements PostMonsterDeathSubscriber {
    public static final String ID = Cirrus.class.getSimpleName();
    
    int soulSplitCount = 10;
    
    public Cirrus(float x, float y) {
        super(ID, 400, 410, x, y);
        type = EnemyType.ELITE;
        floatIndex = AbstractDungeon.monsterRng.randomBoolean() ? -1 : 1;
        bgm = "Dancing Fantasms";
        dialogX = -120.0F * Settings.scale;
        dialogY = -50.0F * Settings.scale;

        addSlot(-400, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(-100, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(200, AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = Cirrus::getMonster;
        
        addMove(Intent.UNKNOWN, mi -> {
            shoutIf(0, 33);
            spawnMonsters();
        });
        
        if (BaseMod.hasModID("spireTogether:")) {
            setHp(maxHealth / 2);
        }
    }
    
    public Cirrus(boolean enhanced){
        this(-100, 300);
        if (enhanced)
            SubscriptionManager.subscribe(this);
    }
    
    public Cirrus() {
        this(-100, 300);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SoulsplitPower(this, soulSplitCount)));
        if (specialAs)
            spawnMonsters();
    }

    @Override
    public void die(boolean triggerRelics) {
        shout(1);
        SubscriptionManager.unsubscribe(this);
        super.die(triggerRelics);
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
    
    public static AbstractMonster getMonster(MonsterSlot slot) {
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

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this) && !Objects.equals(monster.id, id) && ModHelper.check(this)) {
            spawnMonsters(1);
        }
    }
}
