package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.AnExtraPersonsDiaryPower;

public class AnExtraPersonsDiary extends BaseCard {
    public static final String ID = AnExtraPersonsDiary.class.getSimpleName();
    
    public AnExtraPersonsDiary() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AnExtraPersonsDiaryPower(upgraded), 1));
    }
}