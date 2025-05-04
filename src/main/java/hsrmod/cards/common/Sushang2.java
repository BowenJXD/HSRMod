package hsrmod.cards.common;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.actions.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.CachedCondition;

import java.util.function.Predicate;

public class Sushang2 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Sushang2.class.getSimpleName();

    public Sushang2() {
        super(ID);
        BaseMod.subscribe(this);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ApplyPowerAction(
                        p,
                        p,
                        new BreakEffectPower(p, magicNumber),
                        magicNumber
                )
        );

        boolean b = false;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                if (mo.hasPower(BrokenPower.POWER_ID)) {
                    b = true;
                    break;
                }
            }
        }
        if (b) {
            addToBot(new GainEnergyAction(magicNumber));
            if (upgraded) addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscriptionManager.checkSubscriber(this)
                && AbstractDungeon.player.drawPile.contains(this)
                && abstractPower instanceof BrokenPower) {
            addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard c) {
                    return c == Sushang2.this;
                }
            }));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = CachedCondition.check(CachedCondition.Key.ANY_BROKEN) ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
