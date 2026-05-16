package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;

import java.time.LocalTime;

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

        // if the system time is within 4-7 a.m., unlock the signature
        LocalTime now = LocalTime.now();

        LocalTime start = LocalTime.of(4, 0); // 4:00 AM
        LocalTime end = LocalTime.of(7, 0);   // 7:00 AM

        boolean isBetween = !now.isBefore(start) && now.isBefore(end);

        if (isBetween) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        } else {
            System.out.println("Outside the range");
        }
    }
}
