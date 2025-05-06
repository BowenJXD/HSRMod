package androidTestMod.cardsV2.Preservation;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Quake extends BaseCard {
    public static final String ID = Quake.class.getSimpleName();

    public Quake() {
        super(ID);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m, 
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));
    }
}
