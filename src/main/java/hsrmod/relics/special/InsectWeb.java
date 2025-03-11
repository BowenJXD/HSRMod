package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.relics.BaseRelic;

public class InsectWeb extends BaseRelic {
    public static final String ID = InsectWeb.class.getSimpleName();
    
    int hpLoss = 1;
    int strengthGain = 1;

    public InsectWeb() {
        super(ID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, hpLoss));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, strengthGain)));
    }
}
