package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

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
