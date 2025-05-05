package androidTestMod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class BattlefieldMagicianPower extends PowerPower {
    public static final String POWER_ID = AndroidTestMod.makePath(BattlefieldMagicianPower.class.getSimpleName());
    
    int followUpStack;

    public BattlefieldMagicianPower(boolean upgraded, int followUpStack) {
        super(POWER_ID, upgraded);
        this.followUpStack = followUpStack;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], followUpStack);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) trigger();
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) trigger();
            }
        }
    }
    
    void trigger(){
        flash();
        addToBot(new GainBlockAction(owner, owner, followUpStack));
    }
}
