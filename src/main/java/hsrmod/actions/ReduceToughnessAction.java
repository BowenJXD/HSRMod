package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementType;
import hsrmod.powers.Broken;
import hsrmod.powers.Toughness;

public class ReduceToughnessAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private int toughnessReduction;
    private ElementType elementType;
    private DamageInfo info;

    public ReduceToughnessAction(AbstractCreature target, int toughnessReduction, ElementType elementType, DamageInfo info) {
        this.target = target;
        this.toughnessReduction = toughnessReduction;
        this.duration = DURATION;
        this.elementType = elementType;
        this.info = info;
    }

    public void update() {
        if (this.duration == 0.1F) {
            //
            AbstractPower toughnessPower = this.target.getPower(Toughness.POWER_ID);
            if (toughnessPower != null && toughnessPower.amount <= toughnessReduction) {
                int breakDamage = elementType.getBreakDamage();
                AbstractDungeon.actionManager.addToTop(new BreakDamageAction(target, new DamageInfo(info.owner, breakDamage), attackEffect));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new Broken(target, 1), 1));
            }
            if (toughnessPower != null) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new Toughness(target, -toughnessReduction), -toughnessReduction));
            }
            //
        }

        this.tickDuration();
    }
}
