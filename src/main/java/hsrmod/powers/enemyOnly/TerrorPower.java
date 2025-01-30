package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

import java.util.ArrayList;
import java.util.List;

public class TerrorPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(TerrorPower.class.getSimpleName());

    public AbstractGameAction actionCache = null;

    public TerrorPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount, this.amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (!AbstractDungeon.actionManager.actions.contains(actionCache)) {
            actionCache = new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractCard c = AbstractDungeon.player.hand.getRandomCard(true);
                    if (c != null) {
                        flash();
                        c.setCostForTurn(c.costForTurn + TerrorPower.this.amount);
                        isDone = true;
                    }
                }
            };
            addToBot(actionCache);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        AbstractDungeon.player.hand.group.forEach(c -> c.setCostForTurn(c.costForTurn + amount));
    }
}
