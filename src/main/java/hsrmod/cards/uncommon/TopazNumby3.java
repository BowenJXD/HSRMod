package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueDebuffs.ProofOfDebtPower;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TopazNumby3 extends BaseCard {
    public static final String ID = TopazNumby3.class.getSimpleName();
    
    public TopazNumby3() {
        super(ID);
        // tags.add(FOLLOW_UP);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction((q) -> new RemoveSpecificPowerAction(q, q, ProofOfDebtPower.POWER_ID)));
        addToBot(new ApplyPowerAction(m, p, new ProofOfDebtPower(m, magicNumber)));
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
    }
}
