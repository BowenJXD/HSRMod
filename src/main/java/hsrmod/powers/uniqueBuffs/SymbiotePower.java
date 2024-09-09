package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class SymbiotePower extends PowerPower implements PreBreakDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SymbiotePower.class.getSimpleName());

    public SymbiotePower() {
        super(POWER_ID);
    }
    
    @Override
    public void onInitialApplication() {
        SubscribeManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        SubscribeManager.getInstance().unsubscribe(this);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower) {
            this.flash();
            addToBot(new GainEnergyAction(2));
        }
    }

    @Override
    public float preBreakDamage(float amount, AbstractCreature target) {
        if (SubscribeManager.checkSubscriber(this)) {
            addToBot(new DrawCardAction(1));
        }
        return amount;
    }
}
