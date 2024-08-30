package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.onlyBuffs.IntersegmentalMembranePower;

public class IntersegmentalMembrane extends BaseCard {
    public static final String ID = IntersegmentalMembrane.class.getSimpleName();
    
    public IntersegmentalMembrane() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntersegmentalMembranePower(p,1, magicNumber)));
    }
}
