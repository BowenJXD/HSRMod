package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.SuspicionPower;

public class ThePathless extends BaseCard {
    public static final String ID = ThePathless.class.getSimpleName();
    
    public ThePathless() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(monster, p, new BlockReturnPower(monster, 2)));
            addToBot(new ApplyPowerAction(monster, p, new SuspicionPower(monster, magicNumber)));
        }
    }
}
