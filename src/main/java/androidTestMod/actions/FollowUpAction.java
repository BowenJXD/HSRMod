package androidTestMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.cards.BaseCard;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;

import java.util.function.Predicate;

public class FollowUpAction extends AbstractGameAction {
    private AbstractCard card;
    
    AbstractCreature target;
    
    boolean autoPlay;

    public FollowUpAction(AbstractCard card, AbstractCreature target, boolean autoPlay) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.card = card;
        this.target = target;
        this.autoPlay = autoPlay;
    }
    
    public FollowUpAction(AbstractCard card, AbstractCreature target) {
        this(card, target, true);
    }

    public FollowUpAction(AbstractCard card) {
        this(card, null);
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.actionManager.turnHasEnded) {
                this.isDone = true;
                if (card instanceof BaseCard){
                    ((BaseCard) card).followedUp = false;
                }
                return;
            }

            for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard c) {
                    return c.uuid.equals(card.uuid);
                }
            })) {
                r.group.removeCard(r.card);
            }
            AbstractDungeon.getCurrRoom().souls.remove(card); // ?
            AbstractDungeon.player.limbo.group.add(card);
            card.current_y = -200.0F * Settings.scale;
            card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            card.target_y = (float)Settings.HEIGHT / 2.0F;
            card.targetAngle = 0.0F;
            card.targetDrawScale = 0.75F;
            card.drawScale = 0.12F;
            card.lighten(false);
            card.applyPowers();

            target = SubscriptionManager.getInstance().triggerPreFollowUp(card, target);
            
            if (card instanceof BaseCard){
                ((BaseCard) card).followedUp = true;
            }
            
            if (target == null)
                this.addToTop(new NewQueueCardAction(card, true, false, autoPlay));
            else 
                this.addToTop(new NewQueueCardAction(card, target, false, autoPlay));
            
            this.addToTop(new UnlimboAction(card));
            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_XFAST));
            }

            this.isDone = true;
        }

    }
}
