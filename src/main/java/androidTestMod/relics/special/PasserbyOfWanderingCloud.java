package androidTestMod.relics.special;

import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;

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
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                int sum = 0;
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    int i = c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) ? 1 : 0;
                    sum += i;
                }
                if (sum < 2) {
                    PasserbyOfWanderingCloud.this.addToTop(new LoseEnergyAction(1));
                }
            }
        });
    }
}
