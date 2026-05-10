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
         long currentTime = System.currentTimeMillis();
         long startTime = currentTime - (currentTime % 86400000) + 4 * 3600000; // 4 a.m. today
         long endTime = startTime + 3 * 3600000; // 7 a.m. today
         if (currentTime >= startTime && currentTime < endTime) {
             SignatureHelper.unlock(HSRMod.makePath(ID), true);
         }
    }
}
