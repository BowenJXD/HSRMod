package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.onlyBuffs.PhantomThiefPower;

public class PhantomThief extends BaseCard {
    public static final String ID = PhantomThief.class.getSimpleName();
    
    public PhantomThief() {
        super(ID);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PhantomThiefPower(p, 1, upgraded, magicNumber), 1));
    }
}
