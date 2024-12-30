package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

import java.util.HashMap;
import java.util.Map;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class ChampionsDinnerCatsCradlePower extends PowerPower implements PostPowerApplySubscriber, ICheckUsableSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ChampionsDinnerCatsCradlePower.class.getSimpleName());
    
    int chance = 75;
    Map<AbstractCard, Integer> costMap;
    
    public ChampionsDinnerCatsCradlePower(boolean upgraded, int chance) {
        super(POWER_ID, upgraded);
        this.chance = chance;
        costMap = new HashMap<>();
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        BaseMod.subscribe(this);
        SubscriptionManager.subscribe(this, true);
        ModHelper.findCards((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && !c.hasTag(FOLLOW_UP)).forEach((r) -> processCard(r.card));
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        processCard(card);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        processCard(card);
    }
    
    void processCard(AbstractCard card){
        if (card.hasTag(CustomEnums.ENERGY_COSTING) && !card.hasTag(FOLLOW_UP)) {
            card.tags.add(FOLLOW_UP);
            if (upgraded && card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                costMap.put(card, c.energyCost);
                c.energyCost = 0;
            }
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        BaseMod.unsubscribe(this);
        SubscriptionManager.unsubscribe(this);
        ModHelper.findCards((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && c.hasTag(FOLLOW_UP))
                .forEach((r) -> {
                    r.card.tags.remove(FOLLOW_UP);
                    if (upgraded && r.card instanceof BaseCard && costMap.containsKey(r.card)) {
                        BaseCard c = (BaseCard) r.card;
                        c.energyCost = costMap.get(r.card);
                    }
                });
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (abstractPower instanceof EnergyPower 
                    && abstractPower.amount > 0 
                    && ModHelper.getPowerCount(EnergyPower.POWER_ID) >= EnergyPower.AMOUNT_LIMIT
                    && AbstractDungeon.cardRandomRng.random(99) < chance) {
                AbstractCard card = ModHelper.getRandomElement(
                        AbstractDungeon.player.hand.group, 
                        AbstractDungeon.cardRandomRng, 
                        (c) -> c.hasTag(CustomEnums.ENERGY_COSTING)
                );
                if (card != null) {
                    flash();
                    addToBot(new FollowUpAction(card));
                }
            }
        }
    }

    @Override
    public boolean checkUsable(AbstractCard card) {
        if (SubscriptionManager.checkSubscriber(this) && ((BaseCard) card).followedUp) {
            return true;
        }
        return false;
    }
}
