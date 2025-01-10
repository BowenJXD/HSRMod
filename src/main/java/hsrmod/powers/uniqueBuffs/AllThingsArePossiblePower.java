package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.misc.DoTPower;

import java.util.ArrayList;
import java.util.List;

public class AllThingsArePossiblePower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(AllThingsArePossiblePower.class.getSimpleName());
    
    public AllThingsArePossiblePower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) return;
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY 
                || card.target == AbstractCard.CardTarget.ALL) {
            flash();
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                applyDot(monster);
            }
        }
        else if (card.target == AbstractCard.CardTarget.ENEMY) {
            applyDot(action.target);
        }
    }
    
    void applyDot(AbstractCreature c){
        if (c.isDeadOrEscaped()) return;
        List<DoTPower> dots = new ArrayList<>();
        if (!c.hasPower(BleedingPower.POWER_ID))
            dots.add(new BleedingPower(c, owner, 1));
        if (!c.hasPower(BurnPower.POWER_ID))
            dots.add(new BurnPower(c, owner, 1));
        if (!c.hasPower(ShockPower.POWER_ID))
            dots.add(new ShockPower(c, owner, 1));
        if (!c.hasPower(WindShearPower.POWER_ID))
            dots.add(new WindShearPower(c, owner, 1));
        if (!dots.isEmpty()) {
            flash();
            addToBot(new ApplyPowerAction(c, owner, dots.get(AbstractDungeon.cardRandomRng.random(dots.size() - 1)), 1));
        }
        else if (upgraded) {
            flash();
            addToBot(new TriggerDoTAction(c, AbstractDungeon.player));
        }
    }
}
