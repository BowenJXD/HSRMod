package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SunsetPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class SomethingUntoDeath extends AbstractMonster {
    public static final String ID = SomethingUntoDeath.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int turnCount = 0;
    int[] damages = {9, 9, 49};
    int[] damageTimes = {3, 2, 1};
    int sunsetCount = 3;
    int ssHp = 5;
    int strengthGain = 2;
    int deckSize = 0;
    
    AbstractMonster topLeftMonster;
    AbstractMonster topRightMonster;
    AbstractMonster bottomLeftMonster;
    AbstractMonster bottomRightMonster;
    
    public SomethingUntoDeath() {
        super(NAME, HSRMod.makePath(ID), 199, 0F, -15.0F, 410F, 430F, PathDefine.MONSTER_PATH + ID + ".png", -150, 50);
        this.type = EnemyType.ELITE;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = 70.0F * Settings.scale;


        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            ssHp = 7;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            ssHp = 6;
        } else {
            ssHp = 5;
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, EnergyPower.AMOUNT_LIMIT), EnergyPower.AMOUNT_LIMIT));
                addToBot(new ApplyPowerAction(p, this, new SunsetPower(p, sunsetCount, this::getSpawningSombrousSepulcher)));
                break;
            case 2:
                for (int i = 0; i < damageTimes[1]; i++) {
                    addToBot(new DamageAction(p, damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                break;
            case 3:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new DamageAction(p, damage.get(2), AbstractGameAction.AttackEffect.SMASH));
                }
                break;
            case 4:
                for (int i = 0; i < damageTimes[0]; i++) {
                    addToBot(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 4) {
            case 0:
                if (bottomLeftMonster != null && !bottomLeftMonster.isDead 
                        && bottomRightMonster != null && !bottomRightMonster.isDead 
                        && topLeftMonster != null && !topLeftMonster.isDead 
                        && topRightMonster != null && !topRightMonster.isDead) {
                    turnCount++;
                } else {
                    setMove((byte) 1, Intent.UNKNOWN);
                    break;
                }
            case 1:
                setMove((byte) 2, Intent.ATTACK_BUFF, damage.get(1).base, damageTimes[1], true);
                break;
            case 2:
                int currentDeckSize = AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size();
                deckSize = currentDeckSize;
                this.damage.get(2).base = this.damages[2] - currentDeckSize;
                setMove((byte) 3, Intent.ATTACK, damage.get(2).base);
                break;
            case 3:
                setMove((byte) 4, Intent.ATTACK, damage.get(0).base, damageTimes[0], true);
                break;
        }
        turnCount++;
    }

    @Override
    public void update() {
        super.update();
        this.animY = MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
        if (hasPower(ChargingPower.POWER_ID)) {
            int currentDeckSize = AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size();
            if (deckSize != currentDeckSize) {
                deckSize = currentDeckSize;
                this.damage.get(2).base = this.damages[2] - currentDeckSize;
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base);
                this.createIntent();
            }
        }
    }

    AbstractMonster getSpawningSombrousSepulcher() {
        AbstractMonster result = null;
        if (bottomLeftMonster == null || bottomLeftMonster.isDead || bottomLeftMonster.isDying || bottomLeftMonster.currentHealth <= 0) {
            bottomLeftMonster = new SombrousSepulcher(ssHp, -450, 0);
            result = bottomLeftMonster;
        } else if (bottomRightMonster == null || bottomRightMonster.isDead || bottomRightMonster.isDying || bottomRightMonster.currentHealth <= 0) {
            bottomRightMonster = new SombrousSepulcher(ssHp, 150, 0);
            result = bottomRightMonster;
        } else if (topLeftMonster == null || topLeftMonster.isDead || topLeftMonster.isDying || topLeftMonster.currentHealth <= 0) {
            topLeftMonster = new SombrousSepulcher(ssHp, -450, 300);
            result = topLeftMonster;
        } else if (topRightMonster == null || topRightMonster.isDead || topRightMonster.isDying || topRightMonster.currentHealth <= 0) {
            topRightMonster = new SombrousSepulcher(ssHp, 150, 300);
            result = topRightMonster;
        }
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(result, true));
        return result;
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
