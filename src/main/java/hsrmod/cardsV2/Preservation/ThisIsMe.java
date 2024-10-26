package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class ThisIsMe extends BaseCard {
    public static final String ID = ThisIsMe.class.getSimpleName();
    
    public ThisIsMe() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int dex = magicNumber - ModHelper.getPowerCount(p, DexterityPower.POWER_ID);
        if (dex > 0) addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, dex), dex));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, baseBlock / 2));
    }
}
