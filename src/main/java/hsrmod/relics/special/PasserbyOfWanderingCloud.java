package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
            if (AbstractDungeon.player.hand.group.stream().mapToInt(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) ? 1 : 0).sum() < 2) {
                addToTop(new LoseEnergyAction(1));
            }
        });
    }
}
