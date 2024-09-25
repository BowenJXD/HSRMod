package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.Trailblazer7Power;

public class Trailblazer7 extends BaseCard {
    public static final String ID = Trailblazer7.class.getSimpleName();

    public Trailblazer7() {
        super(ID);        
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new Trailblazer7Power(upgraded)));
    }
}
