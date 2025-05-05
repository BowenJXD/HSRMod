package androidTestMod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.DebuffPower;
import androidTestMod.subscribers.PreBreakSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;

import java.util.Iterator;

public class BefogPower extends DebuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(BefogPower.class.getSimpleName());
    
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
                ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                    @Override
                    public void run() {
                        if (AbstractDungeon.player.hand.isEmpty()) return;
                        Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
                        while (hand.hasNext()) {
                            AbstractCard c = hand.next();
                            if (c.canUpgrade()) {
                                BefogPower.this.addToBot(new UpgradeSpecificCardAction(c));
                            }
                        }
                    }
                });

            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
