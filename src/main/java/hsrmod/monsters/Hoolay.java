package hsrmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.List;

public class Hoolay extends AbstractMonster {
    public static final String ID = Hoolay.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;

    int turnCount = 0;
    int[] damageTimes = {4, 2, 3, 4};
    int[] damages = {2, 2, 2, 4};
    int irateCount = 10;
    int spawnCount = 1;
    AbstractMonster topLeftMonster = null;
    AbstractMonster topRightMonster = null;
    AbstractMonster bottomLeftMonster = null;
    AbstractMonster bottomRightMonster = null;

    public Hoolay() {
        super(NAME, HSRMod.makePath(ID), 200, 0F, -15.0F, 384F, 400F, PathDefine.MONSTER_PATH + ID + ".png", -150, 50);
        this.type = EnemyType.ELITE;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = 70.0F * Settings.scale;

        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            irateCount = 10;
            // spawnCount = 2;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            irateCount = 8;
            // spawnCount = 1;
        } else {
            irateCount = 6;
        }
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
                for (int i = 0; i < damageTimes[0]; i++) {
                    addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                break;
            case 2:
                int r = AbstractDungeon.miscRng.random(1, 2);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + r, 3.0F));

                for (int i = 0; i < damageTimes[1]; i++) {
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
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

                for (int i = 0; i < damageTimes[2]; i++) {
                    addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
                    addToBot(new RemoveSpecificPowerAction(this, this, ChargingPower.POWER_ID));
                    addToBot(new RemoveSpecificPowerAction(this, this, IratePower.POWER_ID));
                } else return;
                break;
        }
        spawn();
    }

    @Override
    protected void getMove(int i) {
        if (this.lastMove((byte) 5)) {
            setMove((byte) 6, Intent.ATTACK, this.damage.get(3).base, damageTimes[3], true);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDead && !m.hasPower(BrokenPower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(m, this, new MultiMovePower(m, 1), 1));
                }
            }
        } else if (hasPower(IratePower.POWER_ID) && getPower(IratePower.POWER_ID).amount == IratePower.stackLimit) {
            addToTop(new RemoveSpecificPowerAction(this, this, IratePower.POWER_ID));
            setMove((byte) 5, Intent.UNKNOWN);
        } else if (!lastMove((byte) 3) && i > ModHelper.getPowerCount(TerrorPower.POWER_ID) * 33) {
            setMove((byte) 3, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(2).base, damageTimes[2], true);
        } else if (!lastMove((byte) 2) && i > AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> m.hasPower(MoonRagePower.POWER_ID) ? 50 : 0).sum()) {
            setMove((byte) 2, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(1).base, damageTimes[1], true);
        } else {
            setMove((byte) 1, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, damageTimes[0], true);
        }

        if (!lastMove((byte) 5) && nextMove != 5 && !hasPower(IratePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(this, this, new IratePower(this, irateCount), irateCount));
        }
    }

    void spawn() {
        if (bottomLeftMonster == null || bottomLeftMonster.isDead || bottomLeftMonster.isDying || bottomLeftMonster.currentHealth <= 0) {
            bottomLeftMonster = new EclipseWolftrooper(-450, 0);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bottomLeftMonster, true));
        } else if (bottomRightMonster == null || bottomRightMonster.isDead || bottomRightMonster.isDying || bottomRightMonster.currentHealth <= 0) {
            bottomRightMonster = new SableclawWolftrooper(150, 0);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bottomRightMonster, true));
        } else if (topLeftMonster == null || topLeftMonster.isDead || topLeftMonster.isDying || topLeftMonster.currentHealth <= 0) {
            topLeftMonster = new EclipseWolftrooper(-450, 300);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(topLeftMonster, true));
        } else if (topRightMonster == null || topRightMonster.isDead || topRightMonster.isDying || topRightMonster.currentHealth <= 0) {
            topRightMonster = new SableclawWolftrooper(150, 300);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(topRightMonster, true));
        }
    }

    @Override
    public void die() {
        super.die();
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (!m.isDead && !m.isDying) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        }
    }
}
