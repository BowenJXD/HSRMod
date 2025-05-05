package androidTestMod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.BattlefieldMagicianPower;

public class BattlefieldMagician extends BaseCard {
    public static final String ID = BattlefieldMagician.class.getSimpleName();
    
    public BattlefieldMagician() {
        super(ID);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BattlefieldMagicianPower(upgraded, magicNumber)));
    }
}
