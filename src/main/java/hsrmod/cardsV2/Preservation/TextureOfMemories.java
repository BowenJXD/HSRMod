/*
package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import hsrmod.cards.BaseCard;

public class TextureOfMemories extends BaseCard {
    public static final String ID = TextureOfMemories.class.getSimpleName();
    
    public TextureOfMemories() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MalleablePower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new MalleablePower(m, magicNumber), magicNumber));
        
        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, baseBlock));
        
        if (upgraded){
            addToBot(new ApplyPowerAction(p, p, new MalleablePower(p, magicNumber), magicNumber));
            addToBot(new GainBlockAction(p, block));
        }
    }
}
*/
