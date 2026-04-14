package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import hsrmod.relics.BaseRelic;

public class ObliterationWickTrimmer extends BaseRelic {
    public static final String ID = ObliterationWickTrimmer.class.getSimpleName();

    public ObliterationWickTrimmer() {
        super(ID);
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        super.onObtainCard(c);
        setCounter(counter + 1);
        AbstractDungeon.player.increaseMaxHp(1, true);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, counter), counter));
    }
}
