package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.uncommon.DrRatio3;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class WisemansFollyPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(WisemansFollyPower.class.getSimpleName());

    public WisemansFollyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.NORMAL
                || info.owner != AbstractDungeon.player
                || AbstractDungeon.actionManager.turnHasEnded) {
            return damageAmount;
        }

        Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();

        int debuffNum = 0;
        int chance = 40;
        for (AbstractPower power : owner.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffNum++;
                chance += 20;
            }
        }

        while (hand.hasNext()) {
            AbstractCard card = hand.next();
            if (card instanceof DrRatio3) {
                DrRatio3 c = (DrRatio3) card;
                if (c.followedUp) continue;
                c.debuffNum = debuffNum;

                if (AbstractDungeon.cardRandomRng.random(100) < chance
                        && owner != null
                        && owner.currentHealth > 0
                        && !owner.isDeadOrEscaped()) {
                    c.followedUp = true;
                    addToBot(new FollowUpAction(card, owner));
                }
            }
        }

        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (AbstractDungeon.player.hand.group.stream().noneMatch(c -> c instanceof DrRatio3)) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onRemove() {
        ModHelper.findCards(c -> c instanceof DrRatio3).forEach(r -> {
            addToBot(new ExhaustSpecificCardAction(r.card, r.group));
        });
    }
}
