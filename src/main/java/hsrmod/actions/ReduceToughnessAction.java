package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementType;
import hsrmod.powers.*;

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
            AbstractPower toughnessPower = this.target.getPower(ToughnessPower.POWER_ID);
            if (toughnessPower != null && toughnessPower.amount <= toughnessReduction) {
                int breakDamage = elementType.getBreakDamage();
                applyBreakingPower();
                AbstractDungeon.actionManager.addToTop(new BreakDamageAction(target, new DamageInfo(info.owner, breakDamage), attackEffect));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BrokenPower(target, 1), 1));
            }
            if (toughnessPower != null) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ToughnessPower(target, -toughnessReduction), -toughnessReduction));
            }
            //
        }

        this.tickDuration();
    }
    
    void applyBreakingPower(){
        AbstractPower power = null;
        int stackNum = 1;
        switch (elementType){
            case Physical:
                power = new BleedPower(target, AbstractDungeon.player, stackNum);
                break;
            case Fire:
                power = new BurnPower(target, AbstractDungeon.player, stackNum);
                break;
            case Lightning:
                power = new ShockPower(target, AbstractDungeon.player, stackNum);
                break;
            case Wind:
                AbstractMonster monster = (AbstractMonster)target;
                if (monster != null) {
                    switch (monster.type) {
                        case NORMAL:
                            break;
                        case ELITE:
                            stackNum = 2;
                            break;
                        case BOSS:
                            stackNum = 3;
                            break;
                        default:
                            break;
                    }
                }
                power = new WindShearPower(target, AbstractDungeon.player, stackNum);
                break;
            default:
                break;
        }
        if (power != null){
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, power, stackNum));
        }
    }
}
