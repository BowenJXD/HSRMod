package androidTestMod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.utils.ModHelper;

import java.util.List;
import java.util.function.Predicate;

public class Sparkle1 extends BaseCard {
    public static final String ID = Sparkle1.class.getSimpleName();

    public Sparkle1() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(energyOnUse));

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                List<ModHelper.FindResult> sparkles = ModHelper.findCards(new Predicate<AbstractCard>() {
                    @Override
                    public boolean test(AbstractCard c) {
                        return c instanceof Sparkle1 && c.uuid != uuid;
                    }
                });
                if (!sparkles.isEmpty()) {
                    Sparkle1.this.addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
                }
                for (ModHelper.FindResult result : sparkles) {
                    result.group.removeCard(result.card);
                    for (AbstractCard abstractCard : p.masterDeck.group) {
                        if (abstractCard.uuid == result.card.uuid) {
                            p.masterDeck.removeCard(abstractCard);
                            break;
                        }
                    }
                }
            }
        });
    }
}
