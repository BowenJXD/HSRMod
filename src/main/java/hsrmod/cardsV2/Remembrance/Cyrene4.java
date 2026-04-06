package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class Cyrene4 extends BaseCard {
    public static final String ID = Cyrene4.class.getSimpleName();

    public Cyrene4() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new IncreaseMaxOrbAction(1));
        addToBot(new DrawCardAction(BaseMod.MAX_HAND_SIZE));
        int num = p.energy.energy - energyOnUse;
        if (num > 0) {
            addToBot(new GainEnergyAction(num));
        }
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, 1000)));
        addToBot(new ApotheosisAction());
        ModHelper.addToBotAbstract(() -> {
            int orbCount = p.maxOrbs - p.filledOrbCount();
            for (int i = orbCount; i > 0; i--) {
                addToBot(new ChannelAction(AbstractOrb.getRandomOrb(true)));
            }
        });
    }
}
