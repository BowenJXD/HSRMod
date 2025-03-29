package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
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
        if (m != null)
            addToBot(new VFXAction(new ClashEffect(m.hb.cX, m.hb.cY)));
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
        
        baseDamage = Math.abs(ModHelper.getPowerCount(mo, ToughnessPower.POWER_ID));
        this.calculateCardDamage(mo);
        
        if (damage > 0) {
            addToBot(new ElementalDamageAction(
                    mo,
                    new ElementalDamageInfo(this),
                    AbstractGameAction.AttackEffect.NONE
            ));
        }
        
        // modifyCostForCombat(costCache - costForTurn);
        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
    }

    @Override
    public void onEnterHand() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
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
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this)
                && AbstractDungeon.player.hand.contains(this)) {
            ToughnessPower power = (ToughnessPower) target.getPower(ToughnessPower.POWER_ID);
            if (power != null && !power.isLocked())
                updateCost((int) -amount);
        }
        return amount;
    }
}
