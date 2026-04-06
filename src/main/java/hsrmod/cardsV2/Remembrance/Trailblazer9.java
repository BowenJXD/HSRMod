package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.SubscriptionManager;

public class Trailblazer9 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Trailblazer9.class.getSimpleName();

    int acCharge = 0;
    static final int triggerThreshold = 1000;

    public Trailblazer9() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (isMultiDamage) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        } else {
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
        addToBot(new ChannelAction(new Plasma()));
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        BaseMod.unsubscribe(this);
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == AbstractDungeon.player
                && power.ID.equals(EnergyPower.POWER_ID)
                && power.amount > 0
        ) {
            acCharge += power.amount;
            if (acCharge >= triggerThreshold) {
                acCharge = 0;
                addToTop(new ExhaustToHandAction(this));
            }
        }
    }
}
