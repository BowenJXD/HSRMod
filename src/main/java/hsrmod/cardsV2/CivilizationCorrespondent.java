package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class CivilizationCorrespondent extends BaseCard {
    public static final String ID = CivilizationCorrespondent.class.getSimpleName();
    
    public CivilizationCorrespondent() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int count = ModHelper.getPowerCount(EnergyPower.POWER_ID);
        int blk = count / 10;
        int draw = count / 100;
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, -count), -count));
        addToBot(new GainBlockAction(p, p, blk));
        addToBot(new DrawCardAction(p, draw));
    }
}
