package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.TitForTatPower;
import hsrmod.utils.ModHelper;

public class Gepard extends BaseMonster {
    public static final String ID = Gepard.class.getSimpleName();
    
    int[] block = {10, 36};
    int strengthGain = 2;
    
    public Gepard() {
        super(ID, 300F, 400.0F);
        
        if (ModHelper.specialAscension(type)) {
            turnCount = 2;
        }
        if (ModHelper.moreHPAscension(type)) {
            block = new int[]{10, 40};
        }
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(10, 24, 0);
        }
        else {
            setDamages(10, 20, 0);
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 1: 
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new GainBlockAction(m, block[0]));
                }
                break;
            case 2:
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 3:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new GainBlockAction(m, block[1]));
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
        if (hasPower(ChargingPower.POWER_ID) && !AbstractDungeon.actionManager.turnHasEnded) {
            this.damage.get(2).base = currentBlock;
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, currentBlock);
            this.createIntent();
        }
    }

    @Override
    public void addBlock(int blockAmount) {
        super.addBlock(blockAmount);
        if (hasPower(ChargingPower.POWER_ID) && !AbstractDungeon.actionManager.turnHasEnded) {
            this.damage.get(2).base = currentBlock;
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, currentBlock);
            this.createIntent();
        }
    }
}
