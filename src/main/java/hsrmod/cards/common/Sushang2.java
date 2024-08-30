package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.ModHelper;

public class Sushang2 extends BaseCard {
    public static final String ID = Sushang2.class.getSimpleName();

    public Sushang2() {
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

        ModHelper.addToBotAbstract(() -> {
            ModHelper.addToBotAbstract(() ->{
                int energyAmount = AbstractDungeon.getCurrRoom().monsters.monsters.stream().mapToInt(c -> c.hasPower(BrokenPower.POWER_ID) ? 1 : 0).sum();
                addToBot( new GainEnergyAction(energyAmount) );
            });
        });
    }
}
