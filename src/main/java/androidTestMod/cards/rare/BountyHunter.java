package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.BountyHunterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BountyHunter extends BaseCard {
    public static final String ID = BountyHunter.class.getSimpleName();

    public BountyHunter() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BountyHunterPower(upgraded, magicNumber)));
    }
}
