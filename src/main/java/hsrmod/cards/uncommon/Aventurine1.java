package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.AventurinePower;

public class Aventurine1 extends BaseCard {
    public static final String ID = Aventurine1.class.getSimpleName();
    
    public Aventurine1() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AventurinePower(p, 1, upgraded ? 4 : 3, upgraded ? 3 : 2)));
    }
}
