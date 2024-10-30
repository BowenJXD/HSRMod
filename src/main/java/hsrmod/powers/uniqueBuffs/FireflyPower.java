package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.cards.uncommon.Firefly1;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;

public class FireflyPower extends PowerPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = HSRMod.makePath(FireflyPower.class.getSimpleName());
    
    public FireflyPower() {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this) && ModHelper.getPowerCount(ToughnessPower.POWER_ID) - amount <= 0) {
            this.flash();
            addToBot(new BreakDamageAction(target, new DamageInfo(this.owner, ToughnessPower.getStackLimit(target))));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new BreakEfficiencyPower(this.owner, 1), 1));

            List<ModHelper.FindResult> fireFiles = ModHelper.findCards(c -> c instanceof Firefly1);
            for (ModHelper.FindResult
                    result : fireFiles) {
                if (result.group == AbstractDungeon.player.hand) continue;
                addToBot(new MoveCardsAction(
                        AbstractDungeon.player.hand, result.group, card -> card == result.card));
            }

            if (fireFiles.isEmpty()) {
                addToBot(new MakeTempCardInHandAction(new Firefly1()));
            }
        }
        return amount;
    }
}
