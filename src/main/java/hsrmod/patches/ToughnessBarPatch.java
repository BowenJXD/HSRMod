package hsrmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.ToughnessPower;

@SpirePatch(clz = AbstractCreature.class, method = "renderHealth", paramtypez = {SpriteBatch.class})
public class ToughnessBarPatch {

    private static float drawScale = 2.0F;
    private static float yOffsetBase = 690.0F;
    static float HEALTH_BAR_OFFSET_Y = -30F * Settings.scale;
    static float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    static float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
    static Color blockOutlineColor = new Color(0.8F, 0.8F, 0.8F, 0.8F);
    static Color blockNegativeOutlineColor = new Color(1.0F, 0.6F, 0.6F, 1.0F);
    static float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
    static float HB_Y_OFFSET_DIST = 12.0F * Settings.scale;
    static float hbYOffset = HB_Y_OFFSET_DIST * 2.0F;

    @SpirePostfixPatch
    public static void Postfix(AbstractCreature _inst, SpriteBatch sb) {
        if (_inst instanceof AbstractMonster && _inst.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower tp = (ToughnessPower) _inst.getPower(ToughnessPower.POWER_ID);
            float x = _inst.hb.cX - _inst.hb.width / 2.0F;
            float y = _inst.hb.cY - _inst.hb.height / 2.0F + hbYOffset;
            float ratio = Math.max(0, 1.0F * Math.abs(tp.amount) / ToughnessPower.getStackLimit(_inst));

            renderToughnessBar(sb, (AbstractMonster) _inst, x, y, ratio, tp.amount);

            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont,
                    tp.amount + "/" + ToughnessPower.getStackLimit(_inst),
                    _inst.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale,
                    new Color(0.8F, 0.8F, 0.8F, 1.0F));
        }
    }

    private static void renderToughnessBar(SpriteBatch sb, AbstractMonster m, float x, float y, float ratio, int toughnessAmount) {
        try {

            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 1.0F));
            // Set bar color: red if toughness is less than 0, else the usual color

            // Draw the bar background
            sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, m.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_R, x + m.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

            // Draw the toughness bar from left to right
            if (toughnessAmount <= 0) {
                sb.setColor(blockNegativeOutlineColor); // Red color
            } else {
                sb.setColor(blockOutlineColor);
            }
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ratio * m.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + ratio * m.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        } catch (Exception exception) {
            HSRMod.logger.error(exception.getMessage());
        }
    }
}