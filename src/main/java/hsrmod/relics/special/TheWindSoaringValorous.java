package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.misc.ICanChangeToMulti;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TheWindSoaringValorous extends BaseRelic {
    public static final String ID = TheWindSoaringValorous.class.getSimpleName();

    public int chargeAmount = 40;

    public TheWindSoaringValorous() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)
                    && c instanceof ICanChangeToMulti
                    && c.upgraded
                    && c.target != AbstractCard.CardTarget.ALL_ENEMY) {
                ((ICanChangeToMulti) c).changeToMulti();
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, chargeAmount), chargeAmount));
        }
    }
}
