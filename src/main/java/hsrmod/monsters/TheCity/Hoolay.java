package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import com.megacrit.cardcrawl.vfx.combat.RipAndTearEffect;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.ModHelper;

public class Hoolay extends BaseMonster {
    public static final String ID = Hoolay.class.getSimpleName();

    int[] damageTimes = {4, 2, 3, 4};
    int irateCount = 10;
    int spawnCount = 1;
    AbstractMonster topLeftMonster = null;
    AbstractMonster topRightMonster = null;
    AbstractMonster bottomLeftMonster = null;
    AbstractMonster bottomRightMonster = null;

    public Hoolay() {
        super(ID, 410F, 430F, -150, 50);

        if (ModHelper.moreDamageAscension(type))
            setDamages(2, 2, 3, 4);
        else
            setDamages(1, 2, 3, 4);
        
        if (ModHelper.specialAscension(type)) {
            spawnCount = 1;
        } else {
            spawnCount = 0;
        }
        bgm = "Fatal Clash of Swordgraves";
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        for (int i = 0; i < spawnCount; i++) {
            spawn();
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RollMoveAction(this));
        switch (nextMove) {
            case 1:
                addToBot(new AnimateHopAction(this));
                for (int i = 0; i < damageTimes[0]; i++) {
                    addToBot(new VFXAction(new ClawEffect(p.hb.cX, p.hb.cY, Color.MAROON, Color.SCARLET)));
                    addToBot(new DamageAction(p, this.damage.get(0)));
                }
                break;
            case 2:
                int r = AbstractDungeon.miscRng.random(1, 2);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + r, 3.0F));

                addToBot(new AnimateHopAction(this));
                addToBot(new VFXAction(new ScrapeEffect(p.hb.cX, p.hb.cY)));
                for (int i = 0; i < damageTimes[1]; i++) {
                    addToBot(new DamageAction(p, this.damage.get(1)));
                }
                addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.FIREBRICK, ShockWaveEffect.ShockWaveType.CHAOTIC)));
                AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDead && !m.hasPower(MoonRagePower.POWER_ID)).forEach(m -> {
                    addToBot(new ApplyPowerAction(m, this, new MoonRagePower(m, 1), 1));
                    addToBot(new LoseHPAction(this, this, 4));
                });
                break;
            case 3:
                if (!p.hasPower(TerrorPower.POWER_ID)) {
                    addToBot(new ShoutAction(this, DIALOG[3]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "3", 3.0F));
                }

                addToBot(new AnimateHopAction(this));
                for (int i = 0; i < damageTimes[2]; i++) {
                    addToBot(new VFXAction(new RipAndTearEffect(p.hb.cX, p.hb.cY, Color.MAROON, Color.SCARLET)));
                    addToBot(new DamageAction(p, this.damage.get(2)));
                }
                addToBot(new ApplyPowerAction(p, this, new TerrorPower(p, 1), 1));
                break;
            case 4:
                break;
            case 5:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[6], 1), 1));
                return;
            case 6:
                if (hasPower(ChargingPower.POWER_ID)) {
                    int r1 = AbstractDungeon.miscRng.random(4, 5);
                    addToBot(new ShoutAction(this, DIALOG[r1]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + r1, 3.0F));
                    for (int i = 0; i < damageTimes[3]; i++) {
                        addToBot(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.SMASH));
                    }
                    addToBot(new AnimateHopAction(this));
                    addToBot(new RemoveSpecificPowerAction(this, this, IratePower.POWER_ID));
                } else return;
                break;
        }
        spawn();
    }

    @Override
    protected void getMove(int i) {
        if (this.lastMove((byte) 5)) {
            setMove(MOVES[5], (byte) 6, Intent.ATTACK, this.damage.get(3).base, damageTimes[3], true);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDead && !m.hasPower(BrokenPower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(m, this, new MultiMovePower(m, 1), 1));
                }
            }
        } else if (hasPower(IratePower.POWER_ID) && getPower(IratePower.POWER_ID).amount == IratePower.stackLimit) {
            addToTop(new RemoveSpecificPowerAction(this, this, IratePower.POWER_ID));
            setMove(MOVES[4], (byte) 5, Intent.UNKNOWN);
        } else if (!lastMove((byte) 3) && i > ModHelper.getPowerCount(p, TerrorPower.POWER_ID) * 33) {
            setMove(MOVES[2], (byte) 3, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(2).base, damageTimes[2], true);
        } else if (!lastMove((byte) 2) && i > AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> m.hasPower(MoonRagePower.POWER_ID) ? 50 : 0).sum()) {
            setMove(MOVES[1], (byte) 2, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(1).base, damageTimes[1], true);
        } else {
            setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, damageTimes[0], true);
        }

        if (!lastMove((byte) 5) && nextMove != 5 && !hasPower(IratePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(this, this, new IratePower(this, irateCount), irateCount));
        }
    }

    void spawn() {
        if (bottomLeftMonster == null || bottomLeftMonster.isDead || bottomLeftMonster.isDying || bottomLeftMonster.currentHealth <= 0) {
            bottomLeftMonster = new EclipseWolftrooper(-450, 0);
            bottomLeftMonster.usePreBattleAction();
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bottomLeftMonster, true));
        } else if (bottomRightMonster == null || bottomRightMonster.isDead || bottomRightMonster.isDying || bottomRightMonster.currentHealth <= 0) {
            bottomRightMonster = new SableclawWolftrooper(150, 0);
            bottomRightMonster.usePreBattleAction();
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bottomRightMonster, true));
        } else if (topLeftMonster == null || topLeftMonster.isDead || topLeftMonster.isDying || topLeftMonster.currentHealth <= 0) {
            topLeftMonster = new EclipseWolftrooper(-450, 300);
            topLeftMonster.usePreBattleAction();
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(topLeftMonster, true));
        } else if (topRightMonster == null || topRightMonster.isDead || topRightMonster.isDying || topRightMonster.currentHealth <= 0) {
            topRightMonster = new SableclawWolftrooper(150, 300);
            topRightMonster.usePreBattleAction();
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(topRightMonster, true));
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
