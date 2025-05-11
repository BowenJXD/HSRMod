package hsrmod.cardsV2.Abundance;

import basemod.interfaces.PostPotionUseSubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.NecrosisPower;

public class HeyOverHere extends BaseCard {
    public static final String ID = HeyOverHere.class.getSimpleName();

    public HeyOverHere() {
        super(ID);
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int amt = energyOnUse + (p.hasRelic(ChemicalX.ID) ? 2 : 0);
        addToBot(new AddTemporaryHPAction(p, p, block * amt));
        addToBot(new ApplyPowerAction(p, p, new NecrosisPower(p, amt)));
        if (!freeToPlayOnce)
            addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
