package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
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
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                        ElementType.Ice, 2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        c -> {
                            addToBot(
                                    new ApplyPowerAction(c, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber)
                            );
                        }
                ))
        );
    }
}
