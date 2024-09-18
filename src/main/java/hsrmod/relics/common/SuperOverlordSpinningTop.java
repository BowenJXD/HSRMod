package hsrmod.relics.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.relics.BaseRelic;

public class SuperOverlordSpinningTop extends BaseRelic {
    public static final String ID = SuperOverlordSpinningTop.class.getSimpleName();

    public boolean canTrigger = false;

    public SuperOverlordSpinningTop() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BreakEffectPower(AbstractDungeon.player, 1), 1));
        canTrigger = true;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (canTrigger) {
            flash();
            canTrigger = false;
            AbstractDungeon.getMonsters().monsters.forEach(m -> addToBot(
                    new ApplyPowerAction(m, AbstractDungeon.player,
                            new ToughnessPower(m, -ToughnessPower.getStackLimit(m) / 3), -ToughnessPower.getStackLimit(m) / 3)));
        }
    }
}
