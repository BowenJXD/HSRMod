package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.SuspicionPower;

public class CallOfTheWilderness extends BaseCard {
    public static final String ID = CallOfTheWilderness.class.getSimpleName();
    
    public CallOfTheWilderness() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AllEnemyApplyPowerAction(p, magicNumber, (q) -> new WeakPower(q, magicNumber, false)));
        addToBot(new AllEnemyApplyPowerAction(p, -magicNumber, (q) -> new StrengthPower(q, -magicNumber)));
    }
}
