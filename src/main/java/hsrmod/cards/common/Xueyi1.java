package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Xueyi1 extends BaseCard implements PreToughnessReduceSubscriber {
    public static final String ID = Xueyi1.class.getSimpleName();
    
    int costCache;
    
    public Xueyi1() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        // find the monster with greatest toughness
        AbstractMonster mo = AbstractDungeon.getMonsters().monsters.stream()
                .filter(monster -> !monster.isDead && !monster.isDying)
                .max((monster1, monster2) -> {
                    AbstractPower power1 = monster1.getPower(ToughnessPower.POWER_ID);
                    AbstractPower power2 = monster2.getPower(ToughnessPower.POWER_ID);
                    if (power1 == null && power2 == null) return 0;
                    if (power1 == null) return -1;
                    if (power2 == null) return 1;
                    return power1.amount - power2.amount;
                }).orElse(null);
        if (mo == null) return;
        AbstractPower power = mo.getPower(ToughnessPower.POWER_ID);
        if (power == null) return;
        
        baseDamage = ModHelper.getPowerCount(mo, ToughnessPower.POWER_ID);
        this.calculateCardDamage(mo);
        
        addToBot(new ElementalDamageAction(
                mo, new DamageInfo(p, damage, damageTypeForTurn),
                ElementType.Quantum, magicNumber,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        
        // modifyCostForCombat(costCache - costForTurn);
        updateCost(costCache - costForTurn);
    }

    @Override
    public void onEnterHand() {
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        SubscriptionManager.getInstance().unsubscribe(this);
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
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (!SubscriptionManager.checkSubscriber(this)
                || !AbstractDungeon.player.hand.contains(this)) return amount;
        updateCost((int) -amount);
        return amount;
    }
}
