package hsrmod.cardsV2.TheHunt;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.RandomCardFromDiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class Danheng1 extends BaseCard {
    public static final String ID = Danheng1.class.getSimpleName();
    
    public Danheng1(){
        super(ID);
        energyCost = 60;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        addToBot(new RandomCardFromDiscardPileToHandAction());
        ModHelper.addToBotAbstract(() -> ModHelper.addToBotAbstract(() -> {
            if (p.discardPile.contains(this)) {
                p.discardPile.removeCard(this);
                AbstractDungeon.effectList.add(
                        new ShowCardAndAddToDrawPileEffect(this, (float) Settings.WIDTH / 2.0F, 
                                (float) Settings.HEIGHT / 2.0F, true, true));
            }
        }));
    }
}
