package hsrmod.powers.misc;

import hsrmod.Hsrmod;
import hsrmod.powers.StatePower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;
import java.util.List;

public class EnergyPower extends StatePower {
    public static final String POWER_ID = Hsrmod.makePath(EnergyPower.class.getSimpleName());
    
    static float HEALTH_BAR_OFFSET_Y = -30F * Settings.scale;
    static float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    static float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
    static Color blockOutlineColor = new Color(0.6F, 0.8F, 1.0F, 0.8F);
    static float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
    static float HB_Y_OFFSET_DIST = 12.0F * Settings.scale;
    static float hbYOffset = HB_Y_OFFSET_DIST * 2.0F;
    
    List<Object> lockers;
    boolean locked = false;
    public static final int AMOUNT_LIMIT = 240;

    public EnergyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        if (amount > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        }
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], AMOUNT_LIMIT);
    }

    @Override
    public void stackPower(int stackAmount) {
        alterPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }
    
    void alterPower(int alterAmount) {
        if (locked) {
            return;
        }
        this.fontScale = 8.0F;
        this.amount += alterAmount;
        
        if (amount > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        } else if (amount < 0) {
            amount = 0;
        }
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public void lock(Object locker) {
        if (lockers == null) lockers = new ArrayList<>();
        lockers.add(locker);
        locked = true;
    }

    public void unlock(Object locker) {
        if (lockers != null) lockers.remove(locker);
        if (lockers == null || lockers.isEmpty()) locked = false;
    }
    
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float ix, float iy, Color c) {
        super.renderIcons(sb, ix, iy, c);
        float x = owner.hb.cX - owner.hb.width / 2.0F;
        float y = owner.hb.cY - owner.hb.height / 2.0F + hbYOffset;
        float ratio = Math.max(0, 1.0F * Math.abs(amount) / EnergyPower.AMOUNT_LIMIT);

        try {

            sb.setColor(new Color(0.1F, 0.1F, 0.1F, 1.0F));

            sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, owner.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_R, x + owner.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

            sb.setColor(blockOutlineColor);
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ratio * owner.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + ratio * owner.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        } catch (Exception exception) {
            Hsrmod.logger.error(exception.getMessage());
        }

        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont,
                amount + "/" + EnergyPower.AMOUNT_LIMIT,
                owner.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale,
                new Color(0.8F, 0.8F, 0.8F, 1.0F));
    }
}
