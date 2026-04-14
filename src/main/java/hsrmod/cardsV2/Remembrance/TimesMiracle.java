package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ObsessionPower;
import hsrmod.subscribers.PreOrbPassiveOrEvokeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;

public class TimesMiracle extends BaseCard {
    public static final String ID = TimesMiracle.class.getSimpleName();
    
    public TimesMiracle() {
        super(ID);
        selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }

    @Override
    public void onRetained() {
        super.onRetained();
        AbstractPlayer p = AbstractDungeon.player;
        addToTop(new SelectCardsAction(p.discardPile.group, GeneralUtil.tryFormat(RelicEventHelper.SELECT_TEXT, 1), true, c -> true, (cards) -> {
            List<AbstractCard> cs = new ArrayList<>(cards); 
                addToTop(new MoveCardsAction(p.drawPile, p.discardPile, c -> {
                    return cs.contains(c);
                }));
        }));
        addToTop(new ApplyPowerAction(p, p, new ObsessionPower(p, magicNumber)));

    }
}
