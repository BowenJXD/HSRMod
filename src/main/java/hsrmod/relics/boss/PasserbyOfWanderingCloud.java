package hsrmod.relics.boss;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class PasserbyOfWanderingCloud extends BaseRelic {
    public static final String ID = PasserbyOfWanderingCloud.class.getSimpleName();

    public PasserbyOfWanderingCloud() {
        super(ID);
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }
    
    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        ModHelper.addToBotAbstract(() -> {
            if (AbstractDungeon.player.hand.group.stream().noneMatch(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))) {
                addToTop(new LoseEnergyAction(1));
            }
        });
    }
}
