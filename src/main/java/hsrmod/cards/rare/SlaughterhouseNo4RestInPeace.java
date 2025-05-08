package hsrmod.cards.rare;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.SlaughterhouseNo4RestInPeacePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SlaughterhouseNo4RestInPeace extends BaseCard {
    public static final String ID = SlaughterhouseNo4RestInPeace.class.getSimpleName();
    
    public SlaughterhouseNo4RestInPeace() {
        super(ID);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SlaughterhouseNo4RestInPeacePower(upgraded, magicNumber)));
    }
}
