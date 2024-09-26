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
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;

public class FireflyPower extends PowerPower implements PostPowerApplySubscriber {
    public static final String POWER_ID = HSRMod.makePath(FireflyPower.class.getSimpleName());
    
    public FireflyPower() {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void 
    onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        if (SubscriptionManager.checkSubscriber(this) 
                && abstractPower.ID
                .equals(BrokenPower.POWER_ID)) {
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
    }
}
