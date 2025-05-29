package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Qingque2 extends BaseCard implements PreEnergyChangeSubscriber{
    public static final String ID = Qingque2.class.getSimpleName();

    int costCache = -1;
    private int reduceCostProbability = 50;
    
    public Qingque2() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        costCache = cost;
        isMultiDamage = true;
    }

    @Override
    public void onEnterHand() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void triggerAtStartOfTurn() {
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot(new GainEnergyAction(1));
        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void updateCost(int amt) {
        super.updateCost(amt);
        followUp();
    }

    @Override
    public void modifyCostForCombat(int amt) {
        super.modifyCostForCombat(amt);
        followUp();
    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);
        followUp();
    }

    void followUp(){
        if (!followedUp && costForTurn == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this) && changeAmount < 0) {
            for (int i = 0; i < -changeAmount; i++) {
                if (AbstractDungeon.cardRandomRng.random(100) <= reduceCostProbability) {
                    updateCost(-1);
                    if (costForTurn <= 0) break;
                }
            }
        }
        return changeAmount;
    }
}
