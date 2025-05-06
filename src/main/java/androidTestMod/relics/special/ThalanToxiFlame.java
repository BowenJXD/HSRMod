package androidTestMod.relics.special;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class ThalanToxiFlame extends BaseRelic {
    public static final String ID = ThalanToxiFlame.class.getSimpleName();

    public ThalanToxiFlame() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, magicNumber)));
    }
}
