package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;

public class DisasterHaltingMechanism extends BaseCard {
    public static final String ID = DisasterHaltingMechanism.class.getSimpleName();

    public DisasterHaltingMechanism() {
        super(ID);
        energyCost = 40;
    }

    @Override
    protected void applyPowersToBlock() {
        baseBlock = AbstractDungeon.player.hand.size();
        // if (upgraded) baseBlock += EnergyPanel.getCurrentEnergy();
        super.applyPowersToBlock();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
    }
}
