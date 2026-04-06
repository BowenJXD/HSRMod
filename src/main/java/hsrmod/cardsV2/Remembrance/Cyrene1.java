package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Cyrene1 extends BaseCard {
    public static final String ID = Cyrene1.class.getSimpleName();

    public Cyrene1() {
        super(ID);
        selfRetain = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int focus = upgraded ? p.filledOrbCount() : 1;
        int draw = p.filledOrbCount();
        if (focus > 0) {
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, focus)));
        }
        if (draw > 0) {
            addToBot(new DrawCardAction(p, draw));
        }
    }
}
