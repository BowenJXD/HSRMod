package hsrmod.cards.uncommon;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PreBattleCaregiver extends BaseCard {
    public static final String ID = PreBattleCaregiver.class.getSimpleName();
    
    public PreBattleCaregiver() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int num = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        if (upgraded) num += ModHelper.getPowerCount(p, ToughnessPower.POWER_ID);
        
        int pBlock = num - p.currentBlock;
        if (pBlock > 0) addToBot(new GainBlockAction(p, p, pBlock));
        
        int mBlock = num - m.currentBlock;
        if (mBlock > 0) addToBot(new GainBlockAction(m, p, mBlock));
    }
}
