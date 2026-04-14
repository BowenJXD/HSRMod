package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Hyacine1 extends BaseCard {
    public static final String ID = Hyacine1.class.getSimpleName();

    public Hyacine1() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ChannelAction(new Frost()));
        }
        addToBot(new AddTemporaryHPAction(p, p, block));
    }
}
