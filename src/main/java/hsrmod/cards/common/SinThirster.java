package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;

public class SinThirster extends BaseCard {
    public static final String ID = SinThirster.class.getSimpleName();
    
    public SinThirster() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int max = 0;
        for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!q.isDeadOrEscaped()) {
                int count = 0;
                for (AbstractPower power : q.powers) {
                    if (upgraded ? power.type == AbstractPower.PowerType.DEBUFF : power instanceof DoTPower) {
                        int i = 1;
                        count += i;
                    }
                }
                if (count > max) {
                    max = count;
                }
            }
        }
        
        addToBot(new DrawCardAction(p, max));
    }
}
