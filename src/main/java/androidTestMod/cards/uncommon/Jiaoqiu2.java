package androidTestMod.cards.uncommon;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.breaks.BurnPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Jiaoqiu2 extends BaseCard {
    public static final String ID = Jiaoqiu2.class.getSimpleName();
    
    public Jiaoqiu2() {
        super(ID);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.FIRE
        ));
        addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, 1), 1));
    }
}
