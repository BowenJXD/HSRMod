package hsrmod.monsters.Bonus;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.TheCansCreedPower;

public class KingTrashcan extends BaseMonster {
    public static final String ID = KingTrashcan.class.getSimpleName();

    public KingTrashcan(float x, float y) {
        super(ID, 140, 287, x, y);
        
        addMove(Intent.ATTACK_DEBUFF, 4, mi->{
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            this.damage.get(0).base *= 2;
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
            addToBot(new ApplyPowerAction(this, this, new TheCansCreedPower(this)));
    }

    @Override
    public void die() {
        super.die();
        this.isDying = false;
        if (!isEscaping) {
            addToTop(new EscapeAction(this));
        }
    }

    @Override
    public void escape() {
        super.escape();
        if (AbstractDungeon.getCurrRoom().event == null)
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
