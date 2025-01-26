package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.utils.ModHelper;

public class Spider extends BaseMonster {
    public static final String ID = Spider.class.getSimpleName();
    
    int explosionDmg = 14;
    int selfExpDamage = 10;
    
    public Spider(float x, float y, int turnCount) {
        super(ID, 165F, 197F, x, y);
        this.turnCount = turnCount;
        
        setDamagesWithAscension(7, explosionDmg);
        
        if (ModHelper.specialAscension(type) && turnCount == 0) {
            this.turnCount = 1;
        }
    }
    
    public Spider(float x, float y) {
        this(x, y, 0);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[4], MOVES[5], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDying && !m.isDead) {
                    addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(m, selfExpDamage, DamageInfo.DamageType.THORNS, ElementType.Fire, 2), AbstractGameAction.AttackEffect.FIRE));
                }
            }
        })));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
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
                setMove(MOVES[0], (byte) 1, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.UNKNOWN);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, Intent.ATTACK, this.damage.get(1).base);
                break;
        }
        turnCount++;
    }
}
