package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.breaks.ShockPower;

public class Kafka2 extends BaseCard {
    public static final String ID = Kafka2.class.getSimpleName();
    
    public Kafka2() {
        super(ID);
        selfRetain = true;
        energyCost = 120;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage, damageTypeForTurn), 
                elementType, 2, AbstractGameAction.AttackEffect.LIGHTNING, c ->{
            addToBot(new ApplyPowerAction(c, p, new ShockPower(c, p, 1), 1));
            addToBot(new TriggerDoTAction(c, magicNumber, true));
        })));
    }
}
