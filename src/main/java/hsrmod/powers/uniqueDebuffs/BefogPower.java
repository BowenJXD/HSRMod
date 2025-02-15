package hsrmod.powers.uniqueDebuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class BefogPower extends DebuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BefogPower.class.getSimpleName());
    
    public BefogPower(AbstractCreature owner, int Amount, boolean upgraded) {
        super(POWER_ID, owner, Amount, upgraded);
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (upgraded)
            this.description = String.format(DESCRIPTIONS[1], amount, amount);
        else
            this.description = String.format(DESCRIPTIONS[0], amount, amount);
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
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            this.flash();
            addToBot(new GainEnergyAction(this.amount));
            addToBot(new DrawCardAction(this.amount));

            if (upgraded)
                ModHelper.addToBotAbstract(() -> {
                    if (AbstractDungeon.player.hand.isEmpty()) return;
                    Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
                    while (hand.hasNext()) {
                        AbstractCard c = hand.next();
                        if (c.canUpgrade()) {
                            addToBot(new UpgradeSpecificCardAction(c));
                        }
                    }
                });

            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
