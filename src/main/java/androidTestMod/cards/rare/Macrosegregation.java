package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.DivinityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Macrosegregation extends BaseCard {
    public static final String ID = Macrosegregation.class.getSimpleName();
    
    public Macrosegregation() {
        super(ID);
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DivinityPower(p, 2, magicNumber / 100f), 2));
    }
}
