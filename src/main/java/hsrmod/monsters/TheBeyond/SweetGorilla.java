package hsrmod.monsters.TheBeyond;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.monsters.Exordium.Spider;
import hsrmod.monsters.TheCity.IlluminationDragonfish;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SanctionRatePower;
import hsrmod.powers.enemyOnly.ToughnessProtectionPower;
import hsrmod.utils.ModHelper;

public class SweetGorilla extends BaseMonster {
    public static final String ID = SweetGorilla.class.getSimpleName();
    
    int[] damageTimes = {3, 4};
    int hpLoss = 10;
    
    public SweetGorilla(float x, float y) {
        super(ID, 300, 384, x, y);

        setDamagesWithAscension(6, 8);
        hpLoss = ModHelper.specialAscension(type) ? 8 : 10;
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                spawnHounds();
                break;
            case 2:
                for (int i = 0; i < damageTimes[0]; i++) {
                    addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            case 3:
                addToBot(new ApplyPowerAction(this, this, new ToughnessProtectionPower(this, 1), 1));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                break;
            case 4:
                if (hasPower(StrengthPower.POWER_ID)) {
                    bouncedTime = 0;
                    addToBot(new AnimateHopAction(this));
                    for (int i = 0; i < damageTimes[1]; i++) {
                        addToBot(new DamageCallbackAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, this::takeBouncedDamage));
                    }
                }
                break;
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 4) {
            case 0:
                if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(c -> c instanceof BubbleHound && !c.isDead)) {
                    setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
                    break;
                }
                else {
                    turnCount++;
                }
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base, damageTimes[0], true);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, Intent.BUFF);
                break;
            case 3:
                setMove(MOVES[3], (byte) 4, Intent.ATTACK, this.damage.get(1).base, damageTimes[1], true);
                break;
        }
        turnCount++;
    }
    
    void spawnHounds() {
        BubbleHound hound1 = new BubbleHound(-500, 0);
        addToBot(new SpawnMonsterAction(hound1, true));
        ModHelper.addToBotAbstract(hound1::usePreBattleAction);
        BubbleHound hound2 = new BubbleHound(150, 0);
        addToBot(new SpawnMonsterAction(hound2, true));
        ModHelper.addToBotAbstract(hound2::usePreBattleAction);
    }
    
    int bouncedTime;
    
    void takeBouncedDamage(int dmg) {
        if (dmg <= 0) {
            addToBot(new LoseHPAction(this, this, hpLoss));
            bouncedTime++;
        }
        if (bouncedTime == damageTimes[1]) {
            bouncedTime = 0;
            addToBot(new ApplyPowerAction(this, this, new VulnerablePower(this, 1, true), 1));
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
