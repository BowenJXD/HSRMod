package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class RoadToComets extends BaseRelic {
    public static final String ID = RoadToComets.class.getSimpleName();

    public int energyGain = 20;

    public int blockGain = 2;

    public RoadToComets() {
        super(ID);
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        flash();

        int sum = 0;
        for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
            if (abstractCard.hasTag(CustomEnums.ENERGY_COSTING)) {
                int i = energyGain;
                sum += i;
            }
        }
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new EnergyPower(AbstractDungeon.player, sum), sum));

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                                       @Override
                                       public void run() {
                                           int sum2 = 0;
                                           for (AbstractCard c : AbstractDungeon.player.hand.group) {
                                               if (c.selfRetain || c.retain) {
                                                   int gain = blockGain;
                                                   sum2 += gain;
                                               }
                                           }
                                           RoadToComets.this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, sum2));
                                       }
                                   }
        );
    }
}
