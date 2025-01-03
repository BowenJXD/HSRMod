package hsrmod.cardsV2.TheHunt;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.RandomCardFromDiscardPileToHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.RandomCardFromDrawPileToHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class Danheng1 extends BaseCard {
    public static final String ID = Danheng1.class.getSimpleName();
    
    public AbstractGameAction actionCache;
    
    public Danheng1(){
        super(ID);
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        actionCache = new RandomCardFromDrawPileToHandAction();
        addToTop(actionCache);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        if (actionCache != null) {
            AbstractDungeon.actionManager.actions.remove(actionCache);
            actionCache = null;
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        ModHelper.addToBotAbstract(() -> {
            if (p.hand.isEmpty() && p.drawPile.isEmpty() && p.hasRelic(UnceasingTop.ID)) {
                addToTop(new DrawCardAction(1));
            }
            ModHelper.addToBotAbstract(() -> {
                if (p.discardPile.contains(this)) {
                    p.discardPile.removeCard(this);
                    AbstractDungeon.effectList.add(
                            new ShowCardAndAddToDrawPileEffect(this, (float) Settings.WIDTH / 2.0F,
                                    (float) Settings.HEIGHT / 2.0F, true, true));
                }
            });
        });
    }
}
