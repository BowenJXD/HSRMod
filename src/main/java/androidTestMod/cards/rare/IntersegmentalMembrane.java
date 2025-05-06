package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.IntersegmentalMembranePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IntersegmentalMembrane extends BaseCard {
    public static final String ID = IntersegmentalMembrane.class.getSimpleName();
    
    public IntersegmentalMembrane() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntersegmentalMembranePower(magicNumber)));
    }
}
