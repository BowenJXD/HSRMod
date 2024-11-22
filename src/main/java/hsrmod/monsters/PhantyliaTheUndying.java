package hsrmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.UnawakenedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class PhantyliaTheUndying extends AbstractMonster {
    public static final String ID = PhantyliaTheUndying.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;

    int[] damages = {4, 14, 24, 24};
    int heal = 14;
    int chargeRemove = 100;
    int strengthGain = 4;
    int phase = 1;
    int turnCount = 0;

    public PhantyliaTheUndying() {
        super(NAME, HSRMod.makePath(ID), 200, 0.0F, 30.0F, 400F, 512F, PathDefine.MONSTER_PATH + ID + ".png", 100.0F, 0.0F);
        this.type = EnemyType.BOSS;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 19) {
            strengthGain = 4;
            heal = 14;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            strengthGain = 3;
            heal = 10;
        } else {
            strengthGain = 2;
            heal = 4;
        }
        
        for (int i = 0; i < damages.length; i++) {
            this.damage.add(new DamageInfo(this, damages[i]));
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly(Encounter.DIVINE_SEED + "_1");
            AbstractDungeon.getCurrRoom().cannotLose = true;
        }
        addToBot(new ApplyPowerAction(this, this, new UnawakenedPower(this)));
        spawnAbundanceLotus(false);
        spawnDestructionLotus(false);
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster abundanceLotus = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(AbundanceLotus.ID));
        AbstractMonster destructionLotus = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(DestructionLotus.ID));
        
        switch (this.nextMove) {
            case 1:
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, chargeRemove), chargeRemove));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (m != this) {
                        addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, strengthGain), strengthGain));
                    }
                }
                break;
            case 2:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new HealAction(m, this, heal));
                }
                if (abundanceLotus == null || abundanceLotus.isDead) {
                    spawnAbundanceLotus(false);
                }
                break;
            case 3:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (destructionLotus == null || destructionLotus.isDead) {
                    spawnDestructionLotus(false);
                }
                break;
            case 4:
                this.halfDead = false;
                this.turnCount = 0;
                this.phase = 2;
                addToBot(new ShoutAction(this, DIALOG[0]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_1", 3.0F));
                CardCrawlGame.music.dispose();
                CardCrawlGame.music.playTempBGM(Encounter.DIVINE_SEED + "_2");
                addToBot(new SFXAction("VO_AWAKENEDONE_1"));
                addToBot(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05F, true));
                
                addToBot(new HealAction(this, this, this.maxHealth));
                addToBot(new CanLoseAction());
                if (abundanceLotus != null && !abundanceLotus.isDead) {
                    addToBot(new RemoveAllPowersAction(abundanceLotus, false));
                    addToBot(new SuicideAction(abundanceLotus));
                }
                if (destructionLotus != null && !destructionLotus.isDead) {
                    addToBot(new RemoveAllPowersAction(destructionLotus, false));
                    addToBot(new SuicideAction(destructionLotus));
                }
                spawnAbundanceLotus(true);
                spawnDestructionLotus(true);
                break;
            case 5:
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (abundanceLotus == null || abundanceLotus.isDead) {
                    spawnAbundanceLotus(true);
                }
                if (destructionLotus == null || destructionLotus.isDead) {
                    spawnDestructionLotus(true);
                }
                break;
            case 6:
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, EnergyPower.AMOUNT_LIMIT), EnergyPower.AMOUNT_LIMIT));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[7], 1), 1));
                break;
            case 7:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new ShoutAction(this, DIALOG[1]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_2", 3.0F));
                    int dmg = this.damages[3];
                    int chargeAmount = ModHelper.getPowerCount(EnergyPower.POWER_ID);
                    dmg -= chargeAmount / 10;
                    int debuffAmount = p.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).mapToInt(power -> power.amount).sum();
                    dmg += debuffAmount * 10;
                    addToBot(new DamageAction(p, new DamageInfo(this, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        AbstractMonster abundanceLotus = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(AbundanceLotus.ID));
        AbstractMonster destructionLotus = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(DestructionLotus.ID));
        
        switch (phase) {
            case 1:
                switch (turnCount % 3) {
                    case 0:
                        this.setMove(MOVES[0], (byte) 1, Intent.ATTACK_BUFF, this.damage.get(0).base);
                        break;
                    case 1:
                        this.setMove(MOVES[1], (byte) 2, Intent.DEFEND);
                        if (abundanceLotus != null)
                            addToBot(new ApplyPowerAction(abundanceLotus, this, new MultiMovePower(abundanceLotus, 1), 1));
                        break;
                    case 2:
                        this.setMove(MOVES[2], (byte) 3, Intent.ATTACK, this.damage.get(1).base);
                        if (destructionLotus != null)
                            addToBot(new ApplyPowerAction(destructionLotus, this, new MultiMovePower(destructionLotus, 1), 1));
                        break;
                }
                break;
            case 2:
                switch (turnCount % 3) {
                    case 0:
                        this.setMove(MOVES[4], (byte) 5, Intent.ATTACK, this.damage.get(2).base);
                        if (abundanceLotus != null)
                            addToBot(new ApplyPowerAction(abundanceLotus, this, new MultiMovePower(abundanceLotus, 1), 1));
                        if (destructionLotus != null)
                            addToBot(new ApplyPowerAction(destructionLotus, this, new MultiMovePower(destructionLotus, 1), 1));
                        break;
                    case 1:
                        this.setMove(MOVES[5], (byte) 6, Intent.UNKNOWN);
                        break;
                    case 2:
                        this.setMove(MOVES[6], (byte) 7, Intent.ATTACK, this.damage.get(3).base);
                        break;
                }
                break;
        }
        turnCount++;
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;
            }

            Iterator s = this.powers.iterator();

            AbstractPower p;
            while (s.hasNext()) {
                p = (AbstractPower) s.next();
                p.onDeath();
            }

            s = AbstractDungeon.player.relics.iterator();

            while (s.hasNext()) {
                AbstractRelic r = (AbstractRelic) s.next();
                r.onMonsterDeath(this);
            }

            this.addToTop(new ClearCardQueueAction());
            s = this.powers.iterator();

            while (true) {
                do {
                    if (!s.hasNext()) {
                        this.setMove((byte) 4, Intent.UNKNOWN);
                        this.createIntent();
                        AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) 4, Intent.UNKNOWN));
                        this.applyPowers();
                        return;
                    }

                    p = (AbstractPower) s.next();
                } while (p.type != AbstractPower.PowerType.DEBUFF && !p.ID.equals("Unawakened"));

                s.remove();
            }
        }
    }
    
    void spawnAbundanceLotus(boolean awakened){
        AbstractMonster lotus = new AbundanceLotus(-400.0F, 300.0F, awakened);
        addToBot(new SpawnMonsterAction(lotus, true));
        addToBot(new ApplyPowerAction(lotus, this, new SummonedPower(lotus)));
        addToBot(new ApplyPowerAction(lotus, this, new AbundanceLotusPower(lotus)));
    }
    
    void spawnDestructionLotus(boolean awakened){
        AbstractMonster lotus = new DestructionLotus(-400.0F, 0.0F, awakened);
        addToBot(new SpawnMonsterAction(lotus, true));
        addToBot(new ApplyPowerAction(lotus, this, new SummonedPower(lotus)));
        addToBot(new ApplyPowerAction(lotus, this, new DestructionLotusPower(lotus)));
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);

            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while (var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                    AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                    AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
                }
            }

            this.onBossVictoryLogic();
        }
    }
}
