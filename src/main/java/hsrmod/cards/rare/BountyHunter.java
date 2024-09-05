package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.BountyHunterPower;

public class BountyHunter extends BaseCard {
    public static final String ID = BountyHunter.class.getSimpleName();

    public BountyHunter() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BountyHunterPower(p, 1, upgraded, magicNumber), 1));
    }
}
