package hsrmod.monsters.TheCity;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.UnawakenedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.Objects;

public class Phantylia extends BaseMonster implements PostPowerApplySubscriber {
    public static final String ID = Phantylia.class.getSimpleName();

    int heal = 14;
    int chargeRemove = -100;
    int strengthGain = 4;
    int phase = 1;

    public Phantylia() {
        super(ID, 0.0F, 30.0F, 400F, 500F, 100.0F, 0.0F);

        if (ModHelper.specialAscension(type)) {
            strengthGain = 4;
            heal = 14;
        } else {
            strengthGain = 3;
            heal = 10;
        }
        
        if (ModHelper.moreDamageAscension(type))
            setDamages(4, 14, 24, 34);
        else
            setDamages(4, 14, 24, 24);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        BaseMod.subscribe(this);
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly(Encounter.DIVINE_SEED + "_1");
            AbstractDungeon.getCurrRoom().cannotLose = true;
        }
        // addToBot(new TalkAction(this, DIALOG[0], 4.0F, 5.0F));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_1", 3.0F));
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
                addToBot(new ShoutAction(this, DIALOG[1]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_2", 3.0F));
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, chargeRemove), chargeRemove));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (m != this) {
                        addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, strengthGain), strengthGain));
                    }
                }
                break;
            case 2:
                addToBot(new ShoutAction(this, DIALOG[2]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_3", 3.0F));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new HealAction(m, this, heal));
                }
                if (abundanceLotus == null || abundanceLotus.isDead) {
                    spawnAbundanceLotus(false);
                }
                break;
            case 3:
                addToBot(new ShoutAction(this, DIALOG[3]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_4", 3.0F));
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (destructionLotus == null || destructionLotus.isDead) {
                    spawnDestructionLotus(false);
                }
                break;
            case 4:
                this.halfDead = false;
                this.turnCount = 0;
                this.phase = 2;
                addToBot(new ShoutAction(this, DIALOG[4]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_5", 3.0F));
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
                addToBot(new ShoutAction(this, DIALOG[5]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_6", 3.0F));
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (abundanceLotus == null || abundanceLotus.isDead) {
                    spawnAbundanceLotus(true);
                }
                if (destructionLotus == null || destructionLotus.isDead) {
                    spawnDestructionLotus(true);
                }
                break;
            case 6:
                addToBot(new ShoutAction(this, DIALOG[6]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_7", 3.0F));
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, -EnergyPower.AMOUNT_LIMIT), -EnergyPower.AMOUNT_LIMIT));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[7], 1), 1));
                break;
            case 7:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new ShoutAction(this, DIALOG[7]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_8", 3.0F));
                    addToBot(new DamageAction(p, damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
                        int debuffCount = AbstractDungeon.player.powers.stream().mapToInt(power -> power.type == AbstractPower.PowerType.DEBUFF ? 1 : 0).sum();
                        int chargeAmount = ModHelper.getPowerCount(EnergyPower.POWER_ID);
                        int dmg = this.damages[3] - chargeAmount / 10 + debuffCount * 10;
                        this.setMove(MOVES[6], (byte) 7, Intent.ATTACK, dmg);
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
            BaseMod.unsubscribe(this);
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            
            ModHelper.killAllMinions();

            this.onBossVictoryLogic();
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (!SubscriptionManager.checkSubscriber(this)) return;
        if (hasPower(ChargingPower.POWER_ID) && abstractCreature == AbstractDungeon.player 
                && (abstractPower instanceof EnergyPower || abstractPower.type == AbstractPower.PowerType.DEBUFF)) {
            int debuffCount = abstractCreature.powers.stream().mapToInt(power -> power.type == AbstractPower.PowerType.DEBUFF && !Objects.equals(power.ID, abstractPower.ID) ? 1 : 0).sum();
            if (abstractPower.type == AbstractPower.PowerType.DEBUFF) debuffCount += 1;
            int chargeAmount = ModHelper.getPowerCount(EnergyPower.POWER_ID);
            if (abstractPower instanceof EnergyPower) chargeAmount += abstractPower.amount;
            chargeAmount = Math.min(chargeAmount, EnergyPower.AMOUNT_LIMIT);
            int dmg = this.damages[3] - chargeAmount / 10 + debuffCount * 10;
            this.damage.get(3).base = dmg;
            this.setMove(MOVES[6], (byte) 7, Intent.ATTACK, dmg);
            this.createIntent();
        }
    }
}
