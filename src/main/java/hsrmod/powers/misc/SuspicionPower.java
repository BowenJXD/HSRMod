package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class SuspicionPower extends DebuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SuspicionPower.class.getSimpleName());

    private final float damageIncrementPercentage = 1f / 10f;

    public SuspicionPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscribeManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscribeManager.getInstance().unsubscribe(this);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageIncrementPercentage * amount * 100));
    }
    
    public float incrementDamage(float damage) {
        damage *= (1 + damageIncrementPercentage * this.amount);
        return damage;
    }

    @Override
    public float preDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        if (SubscribeManager.checkSubscriber(this)
                && target == owner) {
            return incrementDamage(amount);
        }
        return amount;
    }
}
