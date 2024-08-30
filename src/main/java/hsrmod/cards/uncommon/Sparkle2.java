package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.patches.relicInterfaces.OnRemoveCardFromMasterDeckPatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Sparkle2 extends BaseCard {
    public static final String ID = Sparkle2.class.getSimpleName();
    
    public Sparkle2() {
        super(ID);
        energyCost = 110;
        exhaust = true;
        selfRetain = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        p.energy.use(energyOnUse);
        int energyNum = Math.min(energyOnUse * 2, p.energy.energy);
        addToBot(new GainEnergyAction(energyNum));

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle2);
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.removeCard(result.card);
            }
        });
    }
}
