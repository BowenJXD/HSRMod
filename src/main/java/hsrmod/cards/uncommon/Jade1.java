package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Jade1 extends BaseCard {
    public static final String ID = Jade1.class.getSimpleName();
    
    public Jade1() {
        super(ID);
        tags.add(FOLLOW_UP);
        isMultiDamage = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = AbstractDungeon.player.hand.size();
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        addToBot(new LoseHPAction(p, p, 2));
        addToBot(new DrawCardAction(p, magicNumber));

        addToBot(
                new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        );
    }

    @Override
    public void triggerAtStartOfTurn() {
        super.triggerAtStartOfTurn();
        if (!AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID) && !this.followedUp) {
            this.followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action.actionType == AbstractGameAction.ActionType.DRAW && !this.followedUp) {
                this.followedUp = true;
                addToBot(new FollowUpAction(this));
                break;
            }
        }
    }
}
