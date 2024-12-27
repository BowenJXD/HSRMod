package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Grizzly extends BaseMonster {
    public static final String ID = Grizzly.class.getSimpleName();
    
    public Grizzly() {
        super(ID, 0F, -15.0F, 384, 384, -100, 0);
        
        if (ModHelper.specialAscension(type)) {
            turnCount = 1;
        } else {
            turnCount = 0;
        }
        
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(14, 10);
        }
        else {
            setDamages(12, 10);
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (turnCount > 0) {
            spawnSpiders();
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                spawnSpiders();
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 3:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 4) {
            case 0:
                if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(c -> c instanceof Spider && !c.isDead)) {
                    setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
                    break;
                }
                else {
                    turnCount++;
                }
            case 1:
                setMove(MOVES[1], (byte) 2, AbstractMonster.Intent.ATTACK, this.damage.get(0).base);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, AbstractMonster.Intent.UNKNOWN);
                break;
            case 3:
                setMove(MOVES[3], (byte) 4, AbstractMonster.Intent.ATTACK, this.damage.get(1).base, 2, true);
                break;
        }
        turnCount++;
    }
    
    void spawnSpiders() {
        Spider spider1 = new Spider(1, -450, 0);
        addToBot(new SpawnMonsterAction(spider1, true));
        ModHelper.addToBotAbstract(spider1::usePreBattleAction);
        Spider spider2 = new Spider(1, 250, 0);
        addToBot(new SpawnMonsterAction(spider2, true));
        ModHelper.addToBotAbstract(spider2::usePreBattleAction);
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
