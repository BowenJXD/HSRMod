package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Sparkle2 extends BaseCard {
    public static final String ID = Sparkle2.class.getSimpleName();
    
    public Sparkle2() {
        super(ID);
        energyCost = 110;
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        p.energy.use(energyOnUse);
        int num = Math.min(energyOnUse * 2, p.energy.energyMaster * 2);
        if (upgraded) num += magicNumber;
        addToBot(new GainEnergyAction(num));

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle2 && c.uuid != uuid);
            if (!sparkles.isEmpty())
                addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.group.stream().filter(c -> c.uuid == result.card.uuid).findFirst().ifPresent(c -> p.masterDeck.removeCard(c));
            }
        });
    }
}
