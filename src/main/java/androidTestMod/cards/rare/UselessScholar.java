package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.UselessScholarPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UselessScholar extends BaseCard {
    public static final String ID = UselessScholar.class.getSimpleName();
    
    public UselessScholar() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new UselessScholarPower(upgraded, magicNumber)));
    }
}
