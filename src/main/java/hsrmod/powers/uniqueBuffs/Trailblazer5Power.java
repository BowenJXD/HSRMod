package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.cards.uncommon.Trailblazer5;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.function.Consumer;

public class Trailblazer5Power extends PowerPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer5Power.class.getSimpleName());

    AbstractCard cardCache;
    
    public Trailblazer5Power() {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        cardCache = card;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        cardCache = null;
    }

    @Override
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (!SubscriptionManager.checkSubscriber(this) 
                || cardCache == null 
                || !target.hasPower(BrokenPower.POWER_ID)) return amount;
        flash();
        cardCache = null;
        DamageInfo info = new DamageInfo(owner, (int)amount);
        addToBot(new BreakDamageAction(target, info).setCallback(new Consumer<AbstractCreature>() {
            @Override
            public void accept(AbstractCreature m) {
                if (info.output >= 70) {
                    SignatureHelper.unlock(HSRMod.makePath(Trailblazer5.ID), true);
                }
            }
        }));
        return amount;
    }
}
