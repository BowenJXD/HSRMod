package hsrmod.cardsV2.Elation;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.misc.SuspicionPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GAMManager;

import java.util.Objects;

public class Tribbie2 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Tribbie2.class.getSimpleName();

    public Tribbie2() {
        super(ID);
        isMultiDamage = true;
        retain = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.FOLLOW_UP);
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
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.FIRE));
        addToBot(new AllEnemyApplyPowerAction(p, 1, (q) -> new VulnerablePower(q, 1, false)));
        if (upgraded) {
            addToBot(new DrawCardAction(p, magicNumber));
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (SubscriptionManager.checkSubscriber(this) 
                && Objects.equals(power.ID, EnergyPower.POWER_ID) 
                && source == AbstractDungeon.player 
                && power.amount < 0) {
            this.followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
