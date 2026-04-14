package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Remembrance.Pollux3;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.Path;
import hsrmod.powers.TerritoryPower;
import hsrmod.utils.PathDefine;

import java.util.List;

public class LostNetherlandPower extends TerritoryPower {
    public static final String POWER_ID = HSRMod.makePath(LostNetherlandPower.class.getSimpleName());

    boolean triggered = false;
    
    public LostNetherlandPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        updateDescription();
        backgroundPath = "HSRModResources/img/scene/LostNetherland1.png";
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >= owner.currentHealth && !triggered) {
            triggered = true;
            AbstractCard card = new Pollux3();
            addToBot(new MakeTempCardInHandAction(card, false, true));
            addToBot(new FollowUpAction(card));
            return 0;
        }
        return damageAmount;
    }
}
