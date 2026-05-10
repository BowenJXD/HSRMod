package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.SubscriptionManager;

import java.util.List;

public class Trailblazer9 extends BaseCard {
    public static final String ID = Trailblazer9.class.getSimpleName();
    
    PostPowerApplySubscriber subscriber;
    boolean subscribed = false;

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


        if (!subscribed) {
            subscribed = true;
            BaseMod.subscribe(new PostBattleSubscriber() {
                @Override
                public void receivePostBattle(AbstractRoom abstractRoom) {
                    if (SubscriptionManager.checkSubscriber(Trailblazer9.this)) {
                        List<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisCombat;
                        if (cards != null 
                                && !cards.isEmpty() 
                                && cards.get(cards.size() - 2) instanceof Trailblazer11 
                                && cards.get(cards.size() - 1) instanceof Demiurge1) {
                            SignatureHelper.unlock(HSRMod.makePath(Trailblazer9.ID), true);
                            SignatureHelper.unlock(HSRMod.makePath(Trailblazer11.ID), true);
                        }
                        BaseMod.unsubscribeLater(this);
                    }
                }
            });
        }
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        BaseMod.unsubscribe(subscriber);
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
        BaseMod.subscribe(subscriber);
    }
}
