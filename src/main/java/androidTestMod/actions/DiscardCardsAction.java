package androidTestMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class DiscardCardsAction extends AbstractGameAction {
    private List<AbstractCard> targetCard;
    private CardGroup group;

    public DiscardCardsAction(List<AbstractCard> targetCard) {
        this.targetCard = new ArrayList<>(targetCard);
        this.actionType = ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public DiscardCardsAction(List<AbstractCard> targetCard, CardGroup group) {
        this.targetCard = new ArrayList<>(targetCard);
        this.group = group;
        this.actionType = ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.group == null) {
                this.group = AbstractDungeon.player.hand;
            }

            for (AbstractCard c : this.targetCard) {
                if (this.group.contains(c)) {
                    this.group.moveToDiscardPile(c);
                    GameActionManager.incrementDiscard(false);
                    c.triggerOnManualDiscard();
                }
            }
        }

        this.tickDuration();
    }
}
