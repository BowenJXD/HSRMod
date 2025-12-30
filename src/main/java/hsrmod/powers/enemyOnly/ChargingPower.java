package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.effects.CustomAuraEffect;
import hsrmod.effects.CustomWrathParticleEffect;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.function.Consumer;

public class ChargingPower extends StatePower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ChargingPower.class.getSimpleName());

    public float particleTimer;
    public float particleTimer2;
    public String move;
    private static long sfxId = -1L;
    public Consumer<ChargingPower> removeCallback;

    public ChargingPower(AbstractCreature owner, String move, int amount) {
        super(POWER_ID, owner, amount);
        priority = 1;
        this.move = move;
        if (amount > 1)
            description = String.format(DESCRIPTIONS[1], move, amount);
        else
            description = String.format(DESCRIPTIONS[0], move);
        this.updateDescription();
        particleTimer = 0.0F;
        particleTimer2 = 0.0F;
    }

    public ChargingPower(AbstractCreature owner, String move) {
        this(owner, move, 1);
    }
    
    public ChargingPower setRemoveCallback(Consumer<ChargingPower> removeCallback) {
        this.removeCallback = removeCallback;
        return this;
    }

    @Override
    public void updateDescription() {
        try{
            if (amount > 1)
                description = String.format(DESCRIPTIONS[1], amount, " #r" + move);
            else
                description = String.format(DESCRIPTIONS[0], " #r" + move);
        } catch (Exception ignore) {
            
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        // addToBot(new VFXAction(new FastingEffect(owner.hb.cX, owner.hb.cY, Color.RED)));
        SubscriptionManager.subscribe(this);
        
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SCARLET, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(owner.hb.cX, owner.hb.cY, "Wrath"));
        // AbstractDungeon.effectsQueue.add(new TopWarningEffect(description));
    }
    
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        this.stopIdleSfx();
        if (removeCallback != null)
            removeCallback.accept(this);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.05F;
                AbstractDungeon.effectsQueue.add(new CustomWrathParticleEffect(owner.hb));
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
            AbstractDungeon.effectsQueue.add(new CustomAuraEffect(owner.hb, new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.1F, 0.2F), 0.1F)));
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        updateDescription();
    }

    @Override
    public void duringTurn() {
        super.duringTurn();
        AbstractDungeon.effectList.add(new BetterWarningSignEffect(owner.hb.cX, owner.hb.cY + owner.hb.y, 3.0f));
        remove(1);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == this.owner) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            if (owner instanceof AbstractMonster)
                ((AbstractMonster) owner).intent = AbstractMonster.Intent.STUN;
        }
    }
}
