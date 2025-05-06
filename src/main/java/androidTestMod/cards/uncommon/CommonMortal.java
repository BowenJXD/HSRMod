package androidTestMod.cards.uncommon;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.CommonMortalPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CommonMortal extends BaseCard {
    public static final String ID = CommonMortal.class.getSimpleName();
    
    public CommonMortal() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CommonMortalPower(magicNumber, upgraded)));
    }
}
