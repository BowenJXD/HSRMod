package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.QuakePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class ResonanceTransferPower extends PowerPower implements PostBreakBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ResonanceTransferPower.class.getSimpleName());

    public int tr = 2;
    
    public ResonanceTransferPower(boolean upgraded, int tr) {
        super(POWER_ID, upgraded);
        this.tr = tr;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], tr, tr);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (!upgraded) SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (upgraded && target.currentBlock > 0) {
            this.flash();
            addToTop(new ApplyPowerAction(owner, owner, new QuakePower(owner, 1), 1));
            if (ModHelper.getPowerCount(target, ToughnessPower.POWER_ID) > tr) {
                addToTop(new ApplyPowerAction(target, owner, new ToughnessPower(target, -tr), -tr));
            }
        }
    }

    @Override
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this) && c != owner && !upgraded) {
            this.flash();
            addToTop(new ApplyPowerAction(owner, owner, new QuakePower(owner, 1), 1));
            if (ModHelper.getPowerCount(c, ToughnessPower.POWER_ID) > tr) {
                addToTop(new ApplyPowerAction(c, owner, new ToughnessPower(c, -tr), -tr));
            }
        }
    }
}
