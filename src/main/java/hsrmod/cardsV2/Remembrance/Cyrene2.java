package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.Objects;

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
        returnToHand = !Objects.equals(orb.ID, Plasma.ORB_ID);

        ModHelper.addToBotAbstract(() -> {
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(c -> c.cardID.equals(HSRMod.makePath(ID))).count() >= 7) {;
                SignatureHelper.unlock(HSRMod.makePath(ID), true);
            }
        });
    }
}
