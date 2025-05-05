package androidTestMod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.misc.BoostPower;
import androidTestMod.utils.ModHelper;

public class SolemnSnare extends BaseCard {
    public static final String ID = SolemnSnare.class.getSimpleName();

    public SolemnSnare() {
        super(ID);
        exhaust = true;
        setBaseEnergyCost(100);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse + (p.hasRelic("Chemical X") ? 2 : 0);
        if (x > 0) {
            addToBot(new ApplyPowerAction(p, p, new BoostPower(p, x), x));
            if (upgraded) addToBot(new DrawCardAction(x));
        }
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                for (AbstractCard c : p.hand.group) {
                    if (c.upgraded)
                        c.setCostForTurn(c.costForTurn - c.timesUpgraded);
                }
            }
        });
        addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
