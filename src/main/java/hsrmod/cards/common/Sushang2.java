package hsrmod.cards.common;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

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

        if (AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .anyMatch(mo -> mo.hasPower(BrokenPower.POWER_ID))) {
            addToBot(new GainEnergyAction(magicNumber));
            if (upgraded) addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscriptionManager.checkSubscriber(this)
                && AbstractDungeon.player.drawPile.contains(this)
                && abstractPower instanceof BrokenPower) {
            addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, c -> c == this));
        }
    }
}
