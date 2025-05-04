package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.QuakePower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ResonanceTransferPower extends PowerPower implements PostBreakBlockSubscriber, DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(ResonanceTransferPower.class.getSimpleName());

    public ResonanceTransferPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0]);
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
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this) && c != owner && !upgraded) {
            trigger(c);
        }
    }

    public void trigger(AbstractCreature c) {
        this.flash();
        addToTop(new ApplyPowerAction(owner, owner, new QuakePower(owner, 1), 1));
        /*ModHelper.findCards(card -> card.isInnate, true)
                .stream()
                .filter(r -> r.group != AbstractDungeon.player.hand)
                .findAny()
                .ifPresent(r -> {
                            addToTop(new MoveCardsAction(AbstractDungeon.player.hand, r.group, card -> card == r.card, 1));
                        }
                );*/
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        if (!(o instanceof AbstractCard)) return false;
        for (AbstractDamageModifier mod : list) {
            if (mod instanceof ResonanceTransferDamageMod) {
                return false;
            }
        }
        return upgraded;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new ResonanceTransferDamageMod(this));
    }

    public static class ResonanceTransferDamageMod extends AbstractDamageModifier {
        ResonanceTransferPower power;

        public ResonanceTransferDamageMod(ResonanceTransferPower power) {
            this.power = power;
        }

        @Override
        public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {
            if (target != null
                    && target != power.owner
                    && blockedAmount > 0
                    && info.owner == power.owner) {
                power.trigger(target);
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new ResonanceTransferDamageMod(power);
        }
    }
}
