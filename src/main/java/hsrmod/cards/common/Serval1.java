package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.DoTPower;

public class Serval1 extends BaseCard {
    public static final String ID = Serval1.class.getSimpleName();

    public Serval1() {
        super(ID);
        energyCost = 100;
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                        ElementType.Lightning, 2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        this::onElementalDamageDealt))
        );
    }
    
    void onElementalDamageDealt(AbstractCreature m) {
        for (AbstractPower power : m.powers) {
            if (power instanceof DoTPower) {
                DoTPower dotPower = (DoTPower) power;
                dotPower.stackPower(1);
            }
        }
    }
}
