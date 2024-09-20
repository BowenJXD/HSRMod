package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Sparkle1 extends BaseCard {
    public static final String ID = Sparkle1.class.getSimpleName();

    public Sparkle1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(EnergyPanel.getCurrentEnergy()));

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle1);
            if (!sparkles.isEmpty())
                addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.group.stream().filter(c -> c.uuid == result.card.uuid).findFirst().ifPresent(c -> p.masterDeck.removeCard(c));
            }
        });
    }
}
