package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.NecrosisPower;

public class Natasha1 extends BaseCard {
    public static final String ID = Natasha1.class.getSimpleName();

    public Natasha1() {
        super(ID);
        setBaseEnergyCost(90);
        tags.add(CustomEnums.ENERGY_COSTING);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, 1, false));
        addToBot(new AddTemporaryHPAction(p, p, block));
        addToBot(new TriggerPowerAction(p.getPower(NecrosisPower.POWER_ID)));
        addToBot(new ApplyPowerAction(p, p, new NecrosisPower(p, 1)));
        addToBot(new DrawCardAction(p, magicNumber));
    }
}
