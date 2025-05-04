package hsrmod.cards.uncommon;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.function.Consumer;
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
        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }

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
                    SignatureHelper.unlock(HSRMod.makePath(ID), true);
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
