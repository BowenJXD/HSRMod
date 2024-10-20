package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

public class Pela1 extends BaseCard {
    public static final String ID = Pela1.class.getSimpleName();

    public Pela1() {
        super(ID);
        energyCost = 100;
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAllAction(p, multiDamage, damageTypeForTurn, elementType, 2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback(c -> {
                    addToBot(new ApplyPowerAction(c, p, new VulnerablePower(c, this.magicNumber, false), this.magicNumber));
                })
        );
    }
}
