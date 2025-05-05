package androidTestMod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.misc.EnergyPower;
import androidTestMod.utils.ModHelper;

public class CivilizationCorrespondent extends BaseCard {
    public static final String ID = CivilizationCorrespondent.class.getSimpleName();
    
    public CivilizationCorrespondent() {
        super(ID);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int count = ModHelper.getPowerCount(p, EnergyPower.POWER_ID);
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, -count), -count));
        
        int blk = count / 10;
        addToBot(new GainBlockAction(p, p, blk));
        
        if (upgraded) {
            int draw = count / 100;
            addToBot(new DrawCardAction(p, draw));
        }
    }
}
