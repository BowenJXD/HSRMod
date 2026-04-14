package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class PermansorTerrae1 extends BaseCard {
    public static final String ID = PermansorTerrae1.class.getSimpleName();

    public PermansorTerrae1() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        for (int i = 0; i < magicNumber; i++)
            addToBot(new ChannelAction(new Frost()));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainBlockAction(m, p, magicNumber));
    }
}
