package hsrmod.cardsV2.Propagation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.cards.BaseCard;
import hsrmod.cards.uncommon.Sparkle1;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Sparkle3 extends BaseCard {
    public static final String ID = Sparkle3.class.getSimpleName();
    
    public Sparkle3() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));

        addToBot(new SelectCardsAction(p.discardPile.group, 1, cardStrings.EXTENDED_DESCRIPTION[1], list -> {
            if (!list.isEmpty()) {
                AbstractCard c = list.get(0);
                AbstractDungeon.actionManager.addToTop(new MoveCardsAction(p.hand, p.discardPile, card -> card == c));
            }
        }));

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle3 && c.uuid != uuid);
            if (!sparkles.isEmpty())
                addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.group.stream().filter(c -> c.uuid == result.card.uuid).findFirst().ifPresent(c -> p.masterDeck.removeCard(c));
            }
        });
    }
}
