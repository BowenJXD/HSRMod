package androidTestMod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class Clara1 extends BaseCard {
    public static final String ID = Clara1.class.getSimpleName();

    boolean canBeUsed = false;

    public Clara1() {
        super(ID);
        tags.add(FOLLOW_UP);
        selfRetain = true;
    }

    /*@Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (canBeUsed || followedUp);
    }*/

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int count = p.exhaustPile.group.size();
        addToBot(new GainBlockAction(p, p, count + block));
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
    public void atTurnStartPreDraw() {
        if (canBeUsed) {
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void tookDamage() {
        super.tookDamage();
        if (!AbstractDungeon.player.hand.contains(this)
                || !AbstractDungeon.actionManager.turnHasEnded) return i;
        canBeUsed = true;
    }
}
