package hsrmod.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class TheGreatSeptimus extends CustomMonster {
    public static final String ID = TheGreatSeptimus.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    private int[] damages = {17, 27, 7, 17};
    int turnCount = 0;
    int blockGain = 77;
    int numDamage = 7;
    int powerAmount = 9;
    int toughnessAmount = 17;
    
    public TheGreatSeptimus() {
        super(NAME, ID, 777, 0F, -15.0F, 512F, 512F, PathDefine.MONSTER_PATH + ID + ".png", -100F, 0.0F);
        this.type = EnemyType.BOSS;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        if (AbstractDungeon.ascensionLevel >= 19) { powerAmount = 9; }
        else if (AbstractDungeon.ascensionLevel >= 4) { powerAmount = 8; }
        else { powerAmount = 7; }

        for (int dmg : damages) {
            this.damage.add(new DamageInfo(this, dmg));
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
        }
        addToBot(new ApplyPowerAction(this, this, new WalkInTheLightPower(this, powerAmount), powerAmount));
        addToBot(new ApplyPowerAction(this, this, new ToughnessPower(this, toughnessAmount, toughnessAmount), toughnessAmount));
        addToBot(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day1", 1));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        
        if (this.nextMove < DIALOG.length) {
            if (nextMove + 1 != DIALOG.length) {
                addToBot(new ShoutAction(this, DIALOG[nextMove], 1.0F, 2.0F));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day" + (this.nextMove + 1), 1));
            }
            else {
                addToBot(new ShoutAction(this, DIALOG[nextMove], 3.0F, 4.0F));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day" + (this.nextMove + 1), 2));
            }
        }
        
        switch (this.nextMove) {
            case 1:
                // AbstractDungeon.topLevelEffectsQueue.add(new PlayVideoEffect(PathDefine.VIDEO_PATH + ID + ".webm"));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                addToBot(new ApplyPowerAction(p, this, new AlienDreamPower(p, 1), 1));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                addToBot(new ApplyPowerAction(p, this, new EnergyDownPower(p, 1), 1));
                break;
            case 3:
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 2, true), 2));
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 2, true), 2));
                break;
            case 4:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[7], 1), 1));
                break;
            case 5:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new RemoveSpecificPowerAction(this, this, ChargingPower.POWER_ID));
                    for (int i = 0; i < numDamage; i++) {
                        addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true));
                    }
                }
                break;
            case 6:
                addToBot(new GainBlockAction(this, this, blockGain));
                addToBot(new ApplyPowerAction(this, this, new IfWeLiveInTheLightPower(this, 1), 1));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[6], 1), 1));
                break;
            case 7:
                addToBot(new RemoveSpecificPowerAction(this, this, ChargingPower.POWER_ID));
                for (int i = 0; i < numDamage; i++) {
                    addToBot(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                addToBot(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                break;
        }
        
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount) {
            case 0: 
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, Intent.DEBUFF);
                break;
            case 3:
                setMove(MOVES[3], (byte) 4, Intent.UNKNOWN);
                break;
            case 4:
                setMove(MOVES[4], (byte) 5, Intent.ATTACK, this.damage.get(2).base, numDamage, true);
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this && !m.isDead && !m.isDying && !m.halfDead && m.currentHealth > 0 && !m.hasPower(BrokenPower.POWER_ID)) {
                        addToBot(new ApplyPowerAction(m, this, new MultiMovePower(m, 1), 1));
                    }
                }
                break;
            case 5:
                setMove(MOVES[5], (byte) 6, Intent.DEFEND_BUFF);
                break;
            case 6:
                setMove(MOVES[6], (byte) 7, Intent.ATTACK, this.damage.get(3).base, numDamage, true);
                break;
        }
        
        turnCount++;
        if (turnCount > 6) { turnCount = 0; }
    }

    @Override
    public void die() {
        AbstractDungeon.getCurrRoom().cannotLose = false;
        Iterator var7 = AbstractDungeon.getMonsters().monsters.iterator();

        while (var7.hasNext()) {
            AbstractMonster m = (AbstractMonster) var7.next();
            if (m != this && !m.isDead) {
                m.hideHealthBar();
                m.die();
            }
        }
        
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        this.onBossVictoryLogic();
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDead && !m.isDying) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        }
    }
}
