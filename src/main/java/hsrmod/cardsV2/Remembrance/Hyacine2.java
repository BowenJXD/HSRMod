package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.Hyacine2Power;

public class Hyacine2 extends BaseCard {
    public static final String ID = Hyacine2.class.getSimpleName();

    public Hyacine2() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        for (int i = magicNumber; i > 0; i--) {
            addToBot(new ChannelAction(new Frost()));
        }
        addToBot(new ApplyPowerAction(p, p, new Hyacine2Power(p, 1), 1));
    }
}
