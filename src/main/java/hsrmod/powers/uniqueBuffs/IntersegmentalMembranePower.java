package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

public class IntersegmentalMembranePower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(IntersegmentalMembranePower.class.getSimpleName());

    int block;
    
    public IntersegmentalMembranePower(int block) {
        super(POWER_ID);
        this.block = block;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.block);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof BaseCard && ((BaseCard) card).followedUp) 
            return;
        int energyChange = card.costForTurn;
        if (card.costForTurn == -1) {
            energyChange = card.energyOnUse;
        }
        if (energyChange > 0) {
            flash();
            addToBot(new GainBlockAction(owner, owner, energyChange * block));
        }
    }
}
