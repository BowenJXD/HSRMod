package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Lingsha2 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Lingsha2.class.getSimpleName();

    public Lingsha2() {
        super(ID);
        tags.add(FOLLOW_UP);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Map<AbstractCreature, Integer> toughnessMap = new HashMap<>();
        for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
            toughnessMap.put(q, ModHelper.getPowerCount(q, ToughnessPower.POWER_ID));
        }

        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback((c) -> {
            if ((!toughnessMap.containsKey(c) || toughnessMap.get(c) > 0)
                    && ModHelper.getPowerCount(c, ToughnessPower.POWER_ID) <= 0) {
                addToBot(new HealAction(p, p, magicNumber));
                if (p.hand.group.stream().noneMatch(card -> card.type == CardType.ATTACK)) return;
                if (upgraded) {
                    addToBot(new SelectCardsInHandAction(cardStrings.EXTENDED_DESCRIPTION[0], card -> card.type == CardType.ATTACK, (cards) -> {
                        if (!cards.isEmpty() && cards.get(0) != null) {
                            addToBot(new FollowUpAction(cards.get(0)));
                        }
                    }));
                } else {
                    ModHelper.addToBotAbstract(() -> p.hand.group.stream().filter(card -> card.type == CardType.ATTACK).findAny()
                            .ifPresent(card -> addToBot(new FollowUpAction(card))));
                }
            }
        }));
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
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscriptionManager.checkSubscriber(this)
                && AbstractDungeon.player.hand.contains(this)
                && abstractPower instanceof BrokenPower
                && !followedUp) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
