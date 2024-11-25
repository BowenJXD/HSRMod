package hsrmod.monsters.TheBeyond;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import hsrmod.monsters.Exordium.Spider;
import hsrmod.monsters.TheCity.IlluminationDragonfish;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SanctionRatePower;
import hsrmod.powers.enemyOnly.ToughnessProtectionPower;
import hsrmod.utils.ModHelper;

public class SweetGorilla extends AbstractMonster {
    public static final String ID = SweetGorilla.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int turnCount = 0;
    int[] damages = {5, 7};
    int[] damageTimes = {3, 4};
    int hpLoss = 8;
    
    public SweetGorilla() {
        super(NAME, HSRMod.makePath(ID), 155, 0F, -15.0F, 384, 384, PathDefine.MONSTER_PATH + ID + ".png", -100, 0);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 19) {
            maxHealth = 165;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            maxHealth = 160;
        } else {
            maxHealth = 155;
        }

        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
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
                if (p.hasPower(StrengthPower.POWER_ID)) {
                    bouncedTime = 0;
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
        BubbleHound hound1 = new BubbleHound(-450, 0);
        addToBot(new SpawnMonsterAction(hound1, true));
        ModHelper.addToBotAbstract(hound1::usePreBattleAction);
        BubbleHound hound2 = new BubbleHound(250, 0);
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
