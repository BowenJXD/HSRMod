package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.powers.BreakEffectPower;
import hsrmod.powers.BrokenPower;

public class Sushang2 extends BaseCard {
    public static final String ID = Sushang2.class.getSimpleName();

    public Sushang2() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new BreakEffectPower(p, magicNumber),
                        magicNumber
                )
        );
        
        int energyAmount = 0;
        for (AbstractCreature c : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (c.hasPower(BrokenPower.POWER_ID)) {
                energyAmount ++;
            }
        }
        
        AbstractDungeon.actionManager.addToBottom( new GainEnergyAction(energyAmount) );
    }
}
