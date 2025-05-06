package androidTestMod.cardsV2.Propagation;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.SerfOfCalamityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SerfOfCalamity extends BaseCard {
    public static final String ID = SerfOfCalamity.class.getSimpleName();
    
    public SerfOfCalamity() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SerfOfCalamityPower(magicNumber)));
    }
}
