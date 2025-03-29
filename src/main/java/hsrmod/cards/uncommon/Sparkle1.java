package hsrmod.cards.uncommon;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;
import me.antileaf.signature.utils.SignatureHelper;

import java.util.List;

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
        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE - 1) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }

        ModHelper.addToBotAbstract(() -> {
            List<ModHelper.FindResult> sparkles = ModHelper.findCards(c -> c instanceof Sparkle1 && c.uuid != uuid);
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
