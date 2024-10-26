package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Preservation.Quake;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.BoostPower;
import hsrmod.utils.ModHelper;

public class SolemnSnare extends BaseCard {
    public static final String ID = SolemnSnare.class.getSimpleName();

    public SolemnSnare() {
        super(ID);
        exhaust = true;
        energyCost = 100;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse > 0)
            addToBot(new ApplyPowerAction(p, p, new BoostPower(p, energyOnUse), energyOnUse));
        if (upgraded)
            addToBot(new DrawCardAction(energyOnUse));
        ModHelper.addToBotAbstract(() -> {
            p.hand.group.forEach(c -> {
                if (c.upgraded) 
                    c.setCostForTurn(c.costForTurn - c.timesUpgraded);
            });
        });
        p.energy.use(EnergyPanel.totalCount);
    }
}
