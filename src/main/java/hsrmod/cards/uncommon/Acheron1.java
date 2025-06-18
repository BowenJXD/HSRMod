package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.GrayscaleScreenEffect;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.signature.utils.internal.SignatureHelperInternal;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Acheron1 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Acheron1.class.getSimpleName();
    
    boolean canTrigger;
    int costCache;
    
    public Acheron1() {
        super(ID);
        isEthereal = true;
        costCache = cost;
        isMultiDamage = true;
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
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (!inHand) return;
        canTrigger = true;
        if (upgraded && c.isEthereal) {
            updateCost(-1);
            retain = true;
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Acheron"));
        CardCrawlGame.sound.play("SlashedDream1");
        addToBot(new VFXAction(new GrayscaleScreenEffect(Settings.FAST_MODE ? 3 : 5)));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));

        AbstractCreature target = ModHelper.betterGetRandomMonster();
        if (target == null) return;

        ElementalDamageAction action = new ElementalDamageAction(
                target,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.LIGHTNING
        ).setModifier(ci -> ci.info.output += ci.target.powers.stream().mapToInt(power -> power.type == AbstractPower.PowerType.DEBUFF ? 1 : 0).sum());
        addToBot(new BouncingAction(target, magicNumber, action, this));
        
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play("SlashedDream2"));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.LIGHTNING)
                .setModifier(ci -> ci.info.output += ci.target.powers.stream().mapToInt(power -> power.type == AbstractPower.PowerType.DEBUFF ? 1 : 0).sum()));

        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        if (SubscriptionManager.checkSubscriber(this)
                && canTrigger 
                && abstractPower.type == AbstractPower.PowerType.DEBUFF
                && target != AbstractDungeon.player) {
            canTrigger = false;
            updateCost(-1);
            retain = true;
        }
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        if (isEthereal && AbstractDungeon.actionManager.turnHasEnded) {
            SignatureHelper.unlock(HSRMod.makePath(Acheron1.ID), false);
            SignatureHelperInternal.setSignatureNotice(CardLibrary.getCard(HSRMod.makePath(ID)), false);
        }
    }
}
