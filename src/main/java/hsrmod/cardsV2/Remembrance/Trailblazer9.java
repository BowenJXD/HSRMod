package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.SubscriptionManager;

public class Trailblazer9 extends BaseCard {
    public static final String ID = Trailblazer9.class.getSimpleName();
    
    PostPowerApplySubscriber subscriber;

    public Trailblazer9() {
        super(ID);
        exhaust = true;
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
        shout(0);
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Trailblazer9"));
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1)));
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
        subscriber = new PostPowerApplySubscriber() {
            @Override
            public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
                if (SubscriptionManager.checkSubscriber(Trailblazer9.this)
                        && target == AbstractDungeon.player
                        && power.ID.equals(EnergyPower.POWER_ID)
                        && power.amount > 0
                ) {
                    baseMagicNumber -= power.amount;
                    initializeDescription();
                    if (baseMagicNumber <= 0) {
                        baseMagicNumber = magicNumber;
                        addToTop(new ExhaustToHandAction(Trailblazer9.this));
                        BaseMod.unsubscribeLater(this);
                    }
                }
            }
        };
        BaseMod.unsubscribe(subscriber);
        BaseMod.subscribe(subscriber);
    }
}
