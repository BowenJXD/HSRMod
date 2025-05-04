package hsrmod.powers.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.effects.CustomAuraEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class BrokenPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(BrokenPower.class.getSimpleName());

    private float damageIncrementPercentage = 1f / 2f; 
    
    private float damageDecrementPercentage = 1f / 4f;
    
    public boolean doReduce = false;
    private float particleTimer;
    private float particleTimer2;
    
    public BrokenPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        priority = 6;
        amount = -1;
        this.particleTimer = 0.0F;
        this.particleTimer2 = 0.0F;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageIncrementPercentage * 100),
                Math.round(damageDecrementPercentage * 100));
    }
    
    @Override
    public void onInitialApplication() {
        this.type = PowerType.DEBUFF;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        /*if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.05F;
                AbstractDungeon.effectsQueue.add(new WrathParticleEffect());
            }
        }*/

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
            AbstractDungeon.effectsQueue.add(new CustomAuraEffect(
                    owner.hb,
                    new Color(MathUtils.random(0.9F, 1.0F), MathUtils.random(0.2F, 0.3F), MathUtils.random(0.4F, 0.5F), 0.0F)));
        }
    }
    
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return Math.round(damage * (1 + damageIncrementPercentage));
        }
        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return Math.round(damage * (1 - damageDecrementPercentage));
        }
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        doReduce = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (doReduce) {
            doReduce = false;
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
