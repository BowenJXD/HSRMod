package hsrmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.TitForTatPower;
import hsrmod.utils.ModHelper;

public class Gepard extends AbstractMonster {
    public static final String ID = Gepard.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int[] damages = {10, 20};
    int block0 = 10;
    int block = 50;
    int strengthGain = 3;
    int turnCount = 0;
    
    public Gepard() {
        super(NAME, HSRMod.makePath(ID), 80, 0.0F, -15.0F, 300F, 400.0F, PathDefine.MONSTER_PATH + ID + ".png", 0.0F, 0.0F);
        this.type = EnemyType.ELITE;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = 70.0F * Settings.scale;
        
        this.damage.add(new DamageInfo(this, damages[0]));
        this.damage.add(new DamageInfo(this, damages[1]));
        this.damage.add(new DamageInfo(this, 0));
        if (AbstractDungeon.ascensionLevel >= 19) {
            strengthGain = 3;
            block = 50;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            strengthGain = 2;
            block = 45;
        } else {
            strengthGain = 1;
            block = 40;
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 1: 
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new GainBlockAction(m, block0));
                }
                break;
            case 2:
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 3:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new GainBlockAction(m, block));
                }
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                addToBot(new ApplyPowerAction(this, this, new TitForTatPower(this, 1, new ElementalDamageInfo(this, 10, null, 6))));
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    int r = AbstractDungeon.miscRng.random(1);
                    addToBot(new ShoutAction(this, DIALOG[r]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                    addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SMASH));
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                    addToBot(new RemoveSpecificPowerAction(this, this, ChargingPower.POWER_ID));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 4) {
            case 0:
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEFEND, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(1).base);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, Intent.DEFEND_BUFF);
                break;
            case 3:
                this.damage.get(2).base = currentBlock;
                setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, this.damage.get(2).base);
                
                break;
        }
        turnCount++;
    }

    @Override
    public void loseBlock(int amount, boolean noAnimation) {
        super.loseBlock(amount, noAnimation);
        if (hasPower(ChargingPower.POWER_ID)) {
            this.damage.get(2).base = currentBlock;
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, currentBlock);
            this.createIntent();
        }
    }

    @Override
    public void addBlock(int blockAmount) {
        super.addBlock(blockAmount);
        if (hasPower(ChargingPower.POWER_ID)) {
            this.damage.get(2).base = currentBlock;
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, currentBlock);
            this.createIntent();
        }
    }
}
