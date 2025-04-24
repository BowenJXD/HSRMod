package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.PostHPUpdateSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Blade1 extends BaseCard implements PostHPUpdateSubscriber {
    public static final String ID = Blade1.class.getSimpleName();

    int costCache = -1;
    boolean canUnlock;

    public Blade1() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
        isMultiDamage = true;
        tags.add(CardTags.HEALING);
        costCache = cost;
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        SubscriptionManager.subscribe(this);
        canUnlock = !SignatureHelper.isUnlocked(cardID);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        canUnlock = false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        canUnlock = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new CleaveEffect()));
        addToBot(new AddTemporaryHPAction(p, p, block));
        addToBot(new TriggerPowerAction(p.getPower(NecrosisPower.POWER_ID)));
        addToBot(new ApplyPowerAction(p, p, new NecrosisPower(p, 1)));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        // addToBot(new VFXAction());
        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
        if (costForTurn == 0 && canUnlock)
            SignatureHelper.unlock(cardID, true);
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

    void followUp() {
        if (!followedUp && costForTurn == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        baseDamage = TempHPField.tempHp.get(AbstractDungeon.player) * magicNumber / 100;
        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void postHPUpdate(AbstractCreature creature) {
        if (SubscriptionManager.checkSubscriber(this)
                && creature == AbstractDungeon.player
                && !AbstractDungeon.actionManager.turnHasEnded) {
            updateCost(-1);
        }
    }
}
