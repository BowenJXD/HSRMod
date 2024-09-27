package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnMyBlockBrokenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.FrozenPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Yanqing2 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Yanqing2.class.getSimpleName();
    
    boolean damaged = false;
    boolean canBeUsed = false;
    
    public Yanqing2() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && (canBeUsed || followedUp);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Ice,
                        1,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_HEAVY,
                        (c) -> {
                            if (AbstractDungeon.cardRandomRng.random(100) <= magicNumber)
                                addToBot(new ApplyPowerAction(c, p, new FrozenPower(c, 1), 1));
                        }
                )
        );
    }

    @Override
    public void atTurnStartPreDraw() {
        if (!damaged) {
            canBeUsed = true;
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        damaged = false;
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this) 
                && AbstractDungeon.player.hand.contains(this))
            ModHelper.addToTopAbstract(() -> {
                if (GameActionManager.damageReceivedThisTurn > 0) damaged = true;
            });
        return i;
    }
}
