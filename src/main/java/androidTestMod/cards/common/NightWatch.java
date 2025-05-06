package androidTestMod.cards.common;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.misc.DoTPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
        
        addToBot(new GainBlockAction(p, max * 3 + block));
    }
}
