package hsrmod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.SuspicionPower;
import hsrmod.powers.uniqueBuffs.ReignOfKeysPower;

public class ReignOfKeys extends BaseCard {
    public static final String ID = ReignOfKeys.class.getSimpleName();
    
    public ReignOfKeys() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ReignOfKeysPower()));
    }
}
