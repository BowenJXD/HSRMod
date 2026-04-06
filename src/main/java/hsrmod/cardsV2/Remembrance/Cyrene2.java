package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Cyrene2 extends BaseCard {
    public static final String ID = Cyrene2.class.getSimpleName();

    int costCache = -1;
    
    public Cyrene2() {
        super(ID);
        selfRetain = true;
        returnToHand = true;
        costCache = cost;
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractOrb orb = AbstractOrb.getRandomOrb(true);
        addToBot(new ChannelAction(orb));
        if (upgraded) addToBot(new TriggerPassiveAction(orb));
        setCostForTurn(costCache);
    }
}
