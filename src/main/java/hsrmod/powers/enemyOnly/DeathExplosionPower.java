package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;

public class DeathExplosionPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(DeathExplosionPower.class.getSimpleName());
    public String[] DESCRIPTIONS;
    public PowerStrings powerStrings;
    public Runnable action;
    public boolean explodeEffect = true;
    
    public DeathExplosionPower(AbstractCreature owner, String name, String description, boolean explodeEffect, Runnable action) {
        this.ID = POWER_ID;
        this.name = name;
        this.DESCRIPTIONS = new String[]{description};
        this.owner = owner;
        this.type = CustomEnums.STATUS;
        this.action = action;
        this.explodeEffect = explodeEffect;
        powerStrings = new PowerStrings();
        powerStrings.NAME = name;
        powerStrings.DESCRIPTIONS = new String[]{description};
        
        this.loadRegion("explosive");
        this.updateDescription();
    }
    
    public DeathExplosionPower(AbstractCreature owner, String name, String description, Runnable action) {
        this(owner, name, description, true, action);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onDeath() {
        super.onDeath();
        if (explodeEffect)
            this.addToBot(new VFXAction(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY), 0.1F));
        action.run();
    }
}
