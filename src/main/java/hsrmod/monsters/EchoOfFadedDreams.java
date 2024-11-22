package hsrmod.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.powers.enemyOnly.WalkInTheLightPower;

import java.util.Iterator;

public class EchoOfFadedDreams extends CustomMonster {
    public static final String ID = EchoOfFadedDreams.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int firstStep = -1;
    
    public EchoOfFadedDreams(int firstStep, float x, float y) {
        super(NAME, HSRMod.makePath(ID), 77, 0F, -15.0F, 84F, 161F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        this.firstStep = (byte) firstStep;
        
        this.damage.add(new DamageInfo(this, 17));
        this.damage.add(new DamageInfo(this, 7));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.getCurrRoom().cannotLose = true;
        onSpawn();
    }
    
    void onSpawn() {
        AbstractPower power = new RegrowPower(this);
        power.description = MOVES[3];
        addToBot(new ApplyPowerAction(this, this, power));
        addToBot(new ApplyPowerAction(this, this, new SummonedPower(this)));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 0: 
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                break;
            case 1:
                addToBot(new MakeTempCardInDrawPileAction(new Imprison(), 1, true, true));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1), 1));
                break;
            case 3:
                if (MathUtils.randomBoolean()) {
                    addToBot(new SFXAction("DARKLING_REGROW_2", MathUtils.random(-0.1F, 0.1F)));
                } else {
                    addToBot(new SFXAction("DARKLING_REGROW_1", MathUtils.random(-0.1F, 0.1F)));
                }

                addToBot(new HealAction(this, this, this.maxHealth));
                this.halfDead = false;
                onSpawn();
                Iterator var1 = AbstractDungeon.player.relics.iterator();

                while(var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var1.next();
                    r.onSpawnMonster(this);
                }
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (halfDead) {
            setMove((byte) 3, Intent.BUFF);
            return;
        }
        if (firstStep != -1) {
            setMove((byte) firstStep, Intent.UNKNOWN);
            firstStep = -1;
            return;
        }
        int r = i % 3;
        if (lastMove((byte) r)) {
            r = (r + 1) % 3;
        }
        switch (r) {
            case 0:
                setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1],(byte) 1, Intent.DEBUFF);
                break;
            case 2:
                setMove(MOVES[2],(byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            Iterator var2 = this.powers.iterator();

            while (var2.hasNext()) {
                AbstractPower p = (AbstractPower) var2.next();
                p.onDeath();
            }

            var2 = AbstractDungeon.player.relics.iterator();

            while (var2.hasNext()) {
                AbstractRelic r = (AbstractRelic) var2.next();
                r.onMonsterDeath(this);
            }

            this.powers.clear();
            boolean allDead = true;
            Iterator var7 = AbstractDungeon.getMonsters().monsters.iterator();

            AbstractMonster m;
            while (var7.hasNext()) {
                m = (AbstractMonster) var7.next();
                if (!m.halfDead) {
                    allDead = false;
                }
                if (m.hasPower(WalkInTheLightPower.POWER_ID)) {
                    addToTop(new ApplyPowerAction(m, m, new WalkInTheLightPower(m, -1), -1));
                }
            }

            if (!allDead) {
                if (this.nextMove != 3) {
                    this.setMove((byte) 3, Intent.BUFF);
                    this.createIntent();
                    addToBot(new SetMoveAction(this, (byte) 3, Intent.UNKNOWN));
                }
            } else {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                this.halfDead = false;
                var7 = AbstractDungeon.getMonsters().monsters.iterator();

                while (var7.hasNext()) {
                    m = (AbstractMonster) var7.next();
                    m.die();
                }
            }
        }
    }
    
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}
