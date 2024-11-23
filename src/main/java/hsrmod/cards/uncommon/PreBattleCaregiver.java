package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class PreBattleCaregiver extends BaseCard {
    public static final String ID = PreBattleCaregiver.class.getSimpleName();
    
    public PreBattleCaregiver() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int num = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        if (upgraded) num += ModHelper.getPowerCount(ToughnessPower.POWER_ID);
        
        int pBlock = num - p.currentBlock;
        if (pBlock > 0) addToBot(new GainBlockAction(p, p, pBlock));
        
        int mBlock = num - m.currentBlock;
        if (mBlock > 0) addToBot(new GainBlockAction(m, p, mBlock));
    }
}
