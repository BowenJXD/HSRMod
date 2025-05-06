package androidTestMod.relics.uncommon;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class PunklordianRegards extends BaseRelic {
    public static final String ID = PunklordianRegards.class.getSimpleName();

    int turnCount = 0;
    
    public PunklordianRegards() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        turnCount = 0;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        turnCount++;
        if (turnCount % 2 == 1) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 2)));
        } else {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, -2)));
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        turnCount = 0;
    }
}
