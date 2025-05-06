package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Dreams0110 extends BaseRelic {
    public static final String ID = Dreams0110.class.getSimpleName();

    public Dreams0110() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber)));
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (ModHelper.getPowerCount(AbstractDungeon.player, StrengthPower.POWER_ID) > 0)
            addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, StrengthPower.POWER_ID, 1));
    }
}
