package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;
import org.apache.logging.log4j.Level;

public class ThisLoveForever extends BaseCard {
    public static final String ID = ThisLoveForever.class.getSimpleName();

    public ThisLoveForever() {
        super(ID);
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(() -> {
            AbstractOrb orb = null;
            if (!AbstractDungeon.player.orbs.isEmpty()) {
                orb = AbstractDungeon.player.orbs.get(0);
                if (!(orb instanceof EmptyOrbSlot)) {
                    if (upgraded) {
                        this.addToTop(new ChannelAction(orb, false));
                    } else {
                        AbstractOrb newOrb = getOrbWithSameClass(orb);
                        if (newOrb != null) {
                            this.addToTop(new ChannelAction(newOrb, false));
                        }
                    }
                    this.addToTop(new EvokeOrbAction(1));
                }
            }
        });
    }

    private static AbstractOrb getOrbWithSameClass(AbstractOrb orb) {
        // Get the orb class's constructor and create a new instance
        try {
            return orb.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            HSRMod.logger.log(Level.ERROR, "Failed to instantiate orb of class: " + orb.getClass().getName(), e);
            return null; // Return null if instantiation fails
        }
    }
}
