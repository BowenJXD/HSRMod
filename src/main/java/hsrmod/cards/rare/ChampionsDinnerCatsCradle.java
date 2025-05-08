package hsrmod.cards.rare;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ChampionsDinnerCatsCradlePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChampionsDinnerCatsCradle extends BaseCard {
    public static final String ID = ChampionsDinnerCatsCradle.class.getSimpleName();
    
    public ChampionsDinnerCatsCradle() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChampionsDinnerCatsCradlePower(upgraded, magicNumber)));
    }
}
