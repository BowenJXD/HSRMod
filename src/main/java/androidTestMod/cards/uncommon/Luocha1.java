package androidTestMod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.CleanAction;
import androidTestMod.actions.SelectCardsInHandAction;
import androidTestMod.cards.BaseCard;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Luocha1 extends BaseCard {
    public static final String ID = Luocha1.class.getSimpleName();
    
    public Luocha1() {
        super(ID);
        selfRetain = true;
        exhaust = true;
        tags.add(CardTags.HEALING);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        addToBot(new HealAction(p, p, magicNumber));
        if (p.currentHealth < p.maxHealth / 2) {
            addToBot(new HealAction(p, p, magicNumber));
        }
        addToBot(new CleanAction(p, 1, true));
        addToBot(new SelectCardsInHandAction(upgraded ? p.hand.size() : 1, cardStrings.EXTENDED_DESCRIPTION[0], upgraded, upgraded, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                return true;
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> cards) {
                for (AbstractCard card : cards) {
                    Luocha1.this.addToBot(new ExhaustSpecificCardAction(card, p.hand));
                }
            }
        }));
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth / 2) {
            glowColor = GOLD_BORDER_GLOW_COLOR;
        } else {
            glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}
