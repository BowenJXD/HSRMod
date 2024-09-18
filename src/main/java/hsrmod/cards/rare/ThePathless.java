package hsrmod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.SuspicionPower;

public class ThePathless extends BaseCard {
    public static final String ID = ThePathless.class.getSimpleName();
    
    public ThePathless() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true; 
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AllEnemyApplyPowerAction(p, 1, (q) -> new BlockReturnPower(q, 1)));
        addToBot(new AllEnemyApplyPowerAction(p, magicNumber, (q) -> new SuspicionPower(q, magicNumber)));
    }
}
