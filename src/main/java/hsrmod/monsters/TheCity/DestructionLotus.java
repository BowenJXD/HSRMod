package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.DestructionLotusPower;
import hsrmod.powers.enemyOnly.SummonedPower;

public class DestructionLotus extends AbstractMonster {
    public static final String ID = DestructionLotus.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    boolean awakened = false;
    int turnCount = 0;
    
    public DestructionLotus(float x, float y, boolean awakened) {
        super(NAME, HSRMod.makePath(ID), 40, 0F, -15.0F, 140F, 150F, PathDefine.MONSTER_PATH + ID + (awakened ? "_2" : "_1") + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.awakened = awakened;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        this.damage.add(new DamageInfo(this, 4));
        this.damage.add(new DamageInfo(this, 4));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SummonedPower(this)));
        addToBot(new ApplyPowerAction(this, this, new DestructionLotusPower(this)));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(p, this, new DrawReductionPower(p, awakened ? 2 : 1), 1));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, awakened ? 2 : 1, true), 1));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 2) {
            case 0:
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
        }
        turnCount++;
    }
}
