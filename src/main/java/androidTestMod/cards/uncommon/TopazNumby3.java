package androidTestMod.cards.uncommon;

import androidTestMod.actions.AOEAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.uniqueDebuffs.ProofOfDebtPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Function;

public class TopazNumby3 extends BaseCard {
    public static final String ID = TopazNumby3.class.getSimpleName();
    
    public TopazNumby3() {
        super(ID);
        // tags.add(FOLLOW_UP);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
            @Override
            public AbstractGameAction apply(AbstractMonster q) {
                return new RemoveSpecificPowerAction(q, q, ProofOfDebtPower.POWER_ID);
            }
        }));
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
