package androidTestMod.relics.boss;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                boolean b = true;
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.color == AbstractCard.CardColor.CURSE || c.type == AbstractCard.CardType.STATUS) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    Plaguenest.this.addToTop(new LoseEnergyAction(1));
                } else {
                    Plaguenest.this.addToTop(new DrawCardAction(1));
                }
            }
        });
    }
}
