package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.only.BefogPower;

public class Lingsha1 extends BaseCard {
    public static final String ID = Lingsha1.class.getSimpleName();

    public Lingsha1() {
        super(ID);
        energyCost = 110;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, p.hasPower(BreakEffectPower.POWER_ID) ? p.getPower(BreakEffectPower.POWER_ID).amount : 0));
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage),
                        ElementType.Fire, 3,
                        AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(m, p, new BefogPower(m, magicNumber), magicNumber));
    }
}
