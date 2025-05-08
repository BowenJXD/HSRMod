package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import hsrmod.Hsrmod;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.MoveCardsAction;
import hsrmod.cards.uncommon.Firefly1;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.function.Predicate;

public class FireflyPower extends PowerPower implements PreBreakSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(FireflyPower.class.getSimpleName());
    
    public boolean triggered = false;
    
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
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            this.flash();
            addToBot(new VFXAction(new FlameBarrierEffect(owner.hb.cX, owner.hb.cY)));
            addToBot(new BreakDamageAction(target, new DamageInfo(this.owner, ToughnessPower.getStackLimit(target))));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new BreakEfficiencyPower(this.owner, 1), 1));

            for (CardQueueItem cqi : AbstractDungeon.actionManager.cardQueue) {
                if (cqi.card instanceof Firefly1) {
                    return;
                }
            }
            if (AbstractDungeon.actionManager.lastCard instanceof Firefly1) {
                return;
            }
            List<ModHelper.FindResult> fireFiles = ModHelper.findCards(new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard c) {
                    return c instanceof Firefly1;
                }
            });
            for (ModHelper.FindResult result : fireFiles) {
                if (result.group == AbstractDungeon.player.hand) return;
                addToBot(new MoveCardsAction(AbstractDungeon.player.hand, result.group, new Predicate<AbstractCard>() {
                    @Override
                    public boolean test(AbstractCard card) {
                        return card == result.card;
                    }
                }));
            }

            if (fireFiles.isEmpty() && !triggered) {
                addToBot(new MakeTempCardInHandAction(new Firefly1()));
                triggered = true;
            }
        }
    }
}
