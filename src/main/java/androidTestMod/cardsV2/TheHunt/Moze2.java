package androidTestMod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;

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
