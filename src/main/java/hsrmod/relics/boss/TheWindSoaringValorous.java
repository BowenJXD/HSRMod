package hsrmod.relics.boss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.misc.ICanChangeToMulti;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class TheWindSoaringValorous extends BaseRelic {
    public static final String ID = TheWindSoaringValorous.class.getSimpleName();

    public TheWindSoaringValorous() {
        super(ID);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        ModHelper.findCards(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && c instanceof ICanChangeToMulti)
                .forEach(r -> ((ICanChangeToMulti) r).changeToMulti());
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BrainInAVatPower(AbstractDungeon.player, 1), 1));
        }
    }
}
