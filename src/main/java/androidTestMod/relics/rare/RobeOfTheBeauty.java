package androidTestMod.relics.rare;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class RobeOfTheBeauty extends BaseRelic {
    public static final String ID = RobeOfTheBeauty.class.getSimpleName();

    public RobeOfTheBeauty() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        int amt = AbstractDungeon.player.gold / magicNumber;
        if (amt > 0) {
            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, amt), amt));
        }
    }
}
