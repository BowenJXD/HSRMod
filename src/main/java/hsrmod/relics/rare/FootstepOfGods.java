package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.modcore.CustomEnums;
import hsrmod.relics.BaseRelic;

public class FootstepOfGods extends BaseRelic {
    public static final String ID = FootstepOfGods.class.getSimpleName();

    boolean triggeredThisTurn = false;
    
    public FootstepOfGods() {
        super(ID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        triggeredThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.hasTag(CustomEnums.CHRYSOS_HEIR) && !triggeredThisTurn) {
            triggeredThisTurn = true;
            flash();
            addToBot(new ChannelAction(AbstractOrb.getRandomOrb(true)));
        }
    }
}
