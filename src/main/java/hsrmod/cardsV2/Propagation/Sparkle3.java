package hsrmod.cardsV2.Propagation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;
import hsrmod.signature.utils.SignatureHelper;

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
            } else {
                SignatureHelper.unlock(HSRMod.makePath(ID), true);
            }
        }));

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle3 && c.uuid != uuid);
            if (!sparkles.isEmpty()) {
                addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
                SignatureHelper.unlock(HSRMod.makePath(ID), true);
            }
            for (ModHelper.FindResult result : sparkles) {
                result.group.removeCard(result.card);
                p.masterDeck.group.stream().filter(c -> c.uuid == result.card.uuid).findFirst().ifPresent(c -> p.masterDeck.removeCard(c));
            }
        });
    }
}
