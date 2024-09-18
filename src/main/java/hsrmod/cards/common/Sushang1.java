package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Sushang1 extends BaseCard {
    public static final String ID = Sushang1.class.getSimpleName();
    
    public Sushang1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
        addToBot(
                new ApplyPowerAction(
                        p,
                        p,
                        new BreakEffectPower(p, magicNumber),
                        magicNumber
                )
        );
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Physical,
                        3,
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                        (c) -> {
                            if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                                    && (toughness > 0 || upgraded)){
                                addToBot(new DrawCardAction(magicNumber));
                            }
                        }
                )
        );
    }
}
