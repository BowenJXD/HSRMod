package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;

public class NightWatch extends BaseCard {
    public static final String ID = NightWatch.class.getSimpleName();

    public NightWatch() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int max = 0;
        for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!q.isDeadOrEscaped()) {
                int count = q.powers.stream()
                        .filter(power -> upgraded ? power.type == AbstractPower.PowerType.DEBUFF : power instanceof DoTPower)
                        .mapToInt(power -> 1).sum();
                if (count > max) {
                    max = count;
                }
            }
        }
        
        addToBot(new GainBlockAction(p, max * block));
    }
}
