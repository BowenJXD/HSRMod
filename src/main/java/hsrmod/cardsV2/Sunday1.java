package hsrmod.cardsV2;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.CleanAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Sunday1 extends BaseCard {
    public static final String ID = Sunday1.class.getSimpleName();
    
    public Sunday1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], c -> c.hasTag(CustomEnums.FOLLOW_UP), list -> {
            if (!list.isEmpty()) list.forEach(c -> addToBot(new FollowUpAction(c)));
        }));
        if (upgraded) addToBot(new CleanAction(p, 1, true));
    }
}
