package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.cardsV2.Preservation.Quake;
import hsrmod.powers.PowerPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Trailblazer8Power extends PowerPower {
    public static final String POWER_ID = Hsrmod.makePath(Trailblazer8Power.class.getSimpleName());
    
    public int percentage = 50;
    
    public Trailblazer8Power(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (card.baseBlock > 0 && card.block > 0 
                && AbstractDungeon.player.hand.size() < 10) {
            Quake quake = new Quake();
            quake.baseDamage = card.block;
            if (upgraded) quake.upgrade();
            addToTop(new MakeTempCardInHandAction(quake));
        }
    }
}
