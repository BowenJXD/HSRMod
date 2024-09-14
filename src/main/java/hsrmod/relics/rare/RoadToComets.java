package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class RoadToComets extends BaseRelic {
    public static final String ID = RoadToComets.class.getSimpleName();

    public int energyGain = 20;

    public int blockGain = 3;

    public RoadToComets() {
        super(ID);
        blockGain = magicNumber;
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        flash();

        int sum = AbstractDungeon.player.hand.group.stream().filter(c -> c instanceof BaseCard && ((BaseCard) c).energyCost > 0).
                mapToInt(c -> energyGain).sum();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new EnergyPower(AbstractDungeon.player, sum), sum));

        ModHelper.addToBotAbstract(() -> {
                    int sum2 = AbstractDungeon.player.hand.group.stream().filter(c -> c.selfRetain || c.retain).
                            mapToInt(c -> blockGain).sum();
                    addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, sum2));
                }
        );
    }
}
