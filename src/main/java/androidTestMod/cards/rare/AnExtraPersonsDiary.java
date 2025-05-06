package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.AnExtraPersonsDiaryPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AnExtraPersonsDiary extends BaseCard {
    public static final String ID = AnExtraPersonsDiary.class.getSimpleName();
    
    public AnExtraPersonsDiary() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AnExtraPersonsDiaryPower(upgraded)));
    }
}
