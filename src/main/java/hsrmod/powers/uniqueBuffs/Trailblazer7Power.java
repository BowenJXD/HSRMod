package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Trailblazer7Power extends PowerPower implements PostBreakBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer7Power.class.getSimpleName());
    
    int enemyBlock = 4;
    int selfBlock = 8;
    boolean canTrigger = true;
    
    public Trailblazer7Power(boolean upgraded) {
        super(POWER_ID, upgraded);
        if (upgraded) selfBlock = 12;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], enemyBlock, selfBlock);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        AbstractMonster m = ModHelper.getRandomMonster((mo) -> mo.intent == AbstractMonster.Intent.ATTACK 
                || mo.intent == AbstractMonster.Intent.ATTACK_BUFF 
                || mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF 
                || mo.intent == AbstractMonster.Intent.ATTACK_DEFEND, true);
        if (m != null) {
            flash();
            addToBot(new GainBlockAction(m, enemyBlock));
        }
        canTrigger = true;
    }

    @Override
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this) && c != owner && canTrigger) {
            flash();
            canTrigger = false;
            addToBot(new GainBlockAction(owner, owner, selfBlock));
        }
    }
}
