package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.SymbiotePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Symbiote extends BaseCard {
    public static final String ID = Symbiote.class.getSimpleName();
    
    public Symbiote() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SymbiotePower(upgraded)));
    }
}
