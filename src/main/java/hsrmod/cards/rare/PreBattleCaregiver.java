package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;

public class PreBattleCaregiver extends BaseCard {
    public static final String ID = PreBattleCaregiver.class.getSimpleName();
    
    public PreBattleCaregiver() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int stackNum = p.hasPower(BreakEffectPower.POWER_ID) ? p.getPower(BreakEffectPower.POWER_ID).amount : 0;
        addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, stackNum), stackNum));
        addToBot(new GainBlockAction(p, stackNum * 2));
    }
}
