package androidTestMod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.cards.uncommon.Aventurine3;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;
import androidTestMod.subscribers.SubscriptionManager;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class AventurinePower extends PowerPower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(AventurinePower.class.getSimpleName());

    int damageStack, followUpStack, stackLimit = 10, triggerAmount = 7;

    boolean isPlayerTurn = true;

    public AventurinePower(boolean upgraded, int damageStack, int followUpStack) {
        super(POWER_ID, upgraded);
        this.damageStack = damageStack;
        this.followUpStack = followUpStack;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], damageStack, followUpStack);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            isPlayerTurn = false;
        }
    }

    @Override
    public void atStartOfTurn() {
        isPlayerTurn = true;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
        }
        if (this.amount >= triggerAmount) {
            trigger();
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)
                && card instanceof BaseCard
                && !(card instanceof Aventurine3)) {
            BaseCard c = (BaseCard) card;
            if (c.followedUp) {
                flash();
                stackPower(followUpStack);
            }
        }
    }

    void trigger() {
        reducePower(triggerAmount);
        if (AbstractDungeon.player.hand.size() < 10) {
            addToTop(new MakeTempCardInHandAction(new Aventurine3()));
        }
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

     
    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this)
                && owner.currentBlock > 0
                && damageInfo.type == DamageInfo.DamageType.NORMAL) {
            flash();
            stackPower(damageStack);
        }
        return i;
    }
}

    
