package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.BarrierPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;

public class Spider extends AbstractMonster {
    public static final String ID = Spider.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int turnCount = 0;
    int dmg = 6;
    int explosionDmg = 20;
    int explosionDmg2 = 10;
    
    public Spider(int turnCount, float x, float y) {
        super(NAME, HSRMod.makePath(ID), 24, 0F, -15.0F, 165F, 197F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.turnCount = turnCount;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        if (AbstractDungeon.ascensionLevel >= 19) {
            dmg = 7;
            explosionDmg = 16;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            dmg = 6;
            explosionDmg = 14;
        } else {
            dmg = 5;
            explosionDmg = 12;
        }
        
        this.damage.add(new DamageInfo(this, dmg));
        this.damage.add(new DamageInfo(this, explosionDmg));
    }
    
    public Spider(float x, float y) {
        this(0, x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[4], MOVES[5], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDying && !m.isDead) {
                    addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(m, explosionDmg2, DamageInfo.DamageType.THORNS, ElementType.Fire, 2), AbstractGameAction.AttackEffect.FIRE));
                }
            }
        })));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[3], 1), 1));
                break;
            case 3:
                if (hasPower(ChargingPower.POWER_ID)) {
                    this.addToBot(new VFXAction(new ExplosionSmallEffect(hb.cX, hb.cY), 0.1F));
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                    addToBot(new SuicideAction(this));
                }
                break;
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 3) {
            case 0:
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 1:
                setMove((byte) 2, Intent.UNKNOWN);
                break;
            case 2:
                setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base);
                break;
        }
        turnCount++;
    }
}
