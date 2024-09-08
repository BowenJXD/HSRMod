package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class SuspicionPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SuspicionPower.class.getSimpleName());

    private final float damageIncrementPercentage = 1f / 10f;

    public SuspicionPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageIncrementPercentage * amount * 100));
    }
    
    public float incrementDamage(float damage) {
        damage *= (1 + damageIncrementPercentage * this.amount);
        return damage;
    }
}
