package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.effects.PersistentImageEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreOrbPassiveOrEvokeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.PathDefine;

import java.util.Objects;

public class Hyacine2Power extends BuffPower implements PreOrbPassiveOrEvokeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(Hyacine2Power.class.getSimpleName());
    
    AbstractGameEffect imgEffect;
    
    public Hyacine2Power(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        imgEffect = new PersistentImageEffect(PathDefine.EFFECT_PATH + "Memosprite/Ika.png", 0.15f * Settings.WIDTH, 0.75f * Settings.HEIGHT, 1);
        AbstractDungeon.effectList.add(imgEffect);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        imgEffect.dispose();
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        super.onEvokeOrb(orb);
        if (Objects.equals(orb.ID, Frost.ORB_ID)) {
            flash();
            addToBot(new AddTemporaryHPAction(owner, owner, orb.evokeAmount));
        }
    }

    @Override
    public int preOrbPassive(AbstractOrb orb, int amountThisTime) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (Objects.equals(orb.ID, Frost.ORB_ID)) {
                flash();
                addToBot(new AddTemporaryHPAction(owner, owner, amountThisTime));
            }
        }
        return amountThisTime;
    }
}
