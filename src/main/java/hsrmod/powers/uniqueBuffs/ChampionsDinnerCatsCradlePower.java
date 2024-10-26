package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.ICheckUsableSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class ChampionsDinnerCatsCradlePower extends PowerPower implements PostPowerApplySubscriber, ICheckUsableSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ChampionsDinnerCatsCradlePower.class.getSimpleName());
    
    public ChampionsDinnerCatsCradlePower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        BaseMod.subscribe(this);
        SubscriptionManager.getInstance().subscribe(this);
        ModHelper.findCards((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && !c.hasTag(FOLLOW_UP)).forEach((r) -> r.card.tags.add(FOLLOW_UP));
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (card.hasTag(CustomEnums.ENERGY_COSTING) && !card.hasTag(FOLLOW_UP))
            card.tags.add(FOLLOW_UP);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        BaseMod.unsubscribe(this);
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (abstractPower instanceof EnergyPower && abstractPower.amount > 0 && ModHelper.getPowerCount(EnergyPower.POWER_ID) >= EnergyPower.AMOUNT_LIMIT) {
                AbstractCard card = ModHelper.getRandomElement(AbstractDungeon.player.hand.group, AbstractDungeon.cardRandomRng, 
                        (c) -> c.hasTag(CustomEnums.ENERGY_COSTING));
                if (card != null)  
                    addToBot(new FollowUpAction(card));
            }
        }
    }

    @Override
    public boolean checkUsable(AbstractCard card) {
        if (SubscriptionManager.checkSubscriber(this)) {
            return upgraded && ((BaseCard) card).followedUp;
        }
        return false;
    }
}
