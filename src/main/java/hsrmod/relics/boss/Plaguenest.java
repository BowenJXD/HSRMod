package hsrmod.relics.boss;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class Plaguenest extends BaseRelic {
    public static final String ID = Plaguenest.class.getSimpleName();

    public Plaguenest() {
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
            if (AbstractDungeon.player.hand.group.stream().noneMatch(c -> c.color == AbstractCard.CardColor.CURSE || c.type == AbstractCard.CardType.STATUS)) {
                addToTop(new LoseEnergyAction(1));
            }
            else {
                addToTop(new DrawCardAction(1));
            }
        });
    }
}
