package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Sparkle2 extends BaseCard {
    public static final String ID = Sparkle2.class.getSimpleName();
    
    public Sparkle2() {
        super(ID);
        energyCost = 110;
        tags.add(CustomEnums.ENERGY_COSTING);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        p.energy.use(energyOnUse);
        int num = Math.min(energyOnUse * 2, p.energy.energy * 2 + magicNumber);

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle2 && c.uuid != uuid);
            if (!sparkles.isEmpty())
                addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
            else {
                CardCrawlGame.sound.play(ID);
                addToTop(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
            }
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.group.stream().filter(c -> c.uuid == result.card.uuid).findFirst().ifPresent(c -> p.masterDeck.removeCard(c));
            }
        });
        addToBot(new GainEnergyAction(num));
    }
}
