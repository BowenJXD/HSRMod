package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class ObedientDracolion extends BaseMonster {
    public static final String ID = ObedientDracolion.class.getSimpleName();
    
    int weakAmount = 1;
    
    public ObedientDracolion(float x, float y) {
        super(ID, 104F, 200F, x, y);
        
        setDamagesWithAscension(5);
        weakAmount = ModHelper.specialAscension(type) ? 2 : 1;
        floatIndex = AbstractDungeon.monsterRng.randomBoolean() ? -1 : 1;
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new AnimateFastAttackAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(p, this, new WeakPower(this, weakAmount, true), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
