package hsrmod.cardsV2.TheHunt;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Moze2 extends BaseCard {
    public static final String ID = Moze2.class.getSimpleName();
    
    public AbstractCreature priorityTarget;
    
    public Moze2(){
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        if (!followedUp) {
            followedUp = true;
            addToBot(new FollowUpAction(this, priorityTarget));
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        addToBot(new DrawCardAction(magicNumber));
    }
}
