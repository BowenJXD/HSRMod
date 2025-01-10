package hsrmod.monsters.Exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.utils.ModHelper;

public class MaskOfNoThought extends BaseMonster {
    public static final String ID = MaskOfNoThought.class.getSimpleName();
    
    int direction = 0;
    
    public MaskOfNoThought(float x, float y) {
        super(ID, 0F, -15.0F, 155, 155, x, y);
        setDamagesWithAscension(5);
        direction = AbstractDungeon.monsterRng.randomBoolean() ? 1 : -1;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[2], MOVES[3], () -> {
            addToBot(new GainEnergyAction(1));
        })));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 1:
                addToBot(new AnimateFastAttackAction(this));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (ModHelper.checkMonster(m)) {
                        addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1)));
                    }
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        this.animY = direction * MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    protected void getMove(int i) {
        if (turnCount == 0 && (i < 50 || ModHelper.specialAscension(type))) {
            turnCount++;
        }
        switch (turnCount % 2) {
            case 0:
                setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 1, Intent.BUFF);
                break;
        }
        turnCount++;
    }
}
