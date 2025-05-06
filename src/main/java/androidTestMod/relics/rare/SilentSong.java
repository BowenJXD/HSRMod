package androidTestMod.relics.rare;

import androidTestMod.powers.breaks.FrozenPower;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SilentSong extends BaseRelic {
    public static final String ID = SilentSong.class.getSimpleName();

    public SilentSong() {
        super(ID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        flash();
        AbstractMonster m = ModHelper.betterGetRandomMonster();
        if (m != null) {
            addToBot(new ApplyPowerAction(m, m, new FrozenPower(m, 1), 1));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        flash();
        AbstractMonster m = ModHelper.betterGetRandomMonster();
        if (m != null) {
            addToBot(new ApplyPowerAction(m, m, new FrozenPower(m, 1), 1));
        }
    }
}
