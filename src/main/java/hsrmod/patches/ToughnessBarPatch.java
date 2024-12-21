package hsrmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import hsrmod.misc.IMultiToughness;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.ToughnessPower;

@SpirePatch(clz = AbstractCreature.class, method = "renderHealth", paramtypez = {SpriteBatch.class})
public class ToughnessBarPatch {

    private static float drawScale = 2.0F;
    private static float yOffsetBase = 690.0F;
    static float HEALTH_BAR_OFFSET_Y = -30F * Settings.scale;
    static float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    static float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
    static Color positiveColor = new Color(0.8F, 0.8F, 0.8F, 0.8F);
    static Color negativeColor = new Color(1.0F, 0.6F, 0.6F, 0.8F);
    static float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
    static float HB_Y_OFFSET_DIST = 12.0F * Settings.scale;
    static float hbYOffsetMonster = HB_Y_OFFSET_DIST * 2.0F;
    static float hbYOffsetPlayer = HB_Y_OFFSET_DIST * 4.0F;

    @SpirePostfixPatch
    public static void Postfix(AbstractCreature _inst, SpriteBatch sb) {
        if (_inst.hasPower(ToughnessPower.POWER_ID) && _inst.hbAlpha > 0.0F && _inst.currentHealth > 0) {
            ToughnessPower tp = (ToughnessPower) _inst.getPower(ToughnessPower.POWER_ID);
            float x = _inst.hb.cX - _inst.hb.width / 2.0F;
            float y = _inst.hb.cY - _inst.hb.height / 2.0F + (_inst instanceof AbstractPlayer ? hbYOffsetPlayer : hbYOffsetMonster);
            float ratio = Math.max(0, 1.0F * Math.abs(tp.amount) / ToughnessPower.getStackLimit(_inst));

            renderToughnessBar(sb, _inst, x, y, ratio, tp);
            String text = tp.amount + "/" + ToughnessPower.getStackLimit(_inst);
            int barCount = _inst.powers.stream().mapToInt(p -> p instanceof IMultiToughness ? ((IMultiToughness)p).getToughnessBarCount() : 0).sum();
            if (barCount > 1) {
                text += "×" + barCount;
            }
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, text,
                    _inst.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale,
                    new Color(0.8F, 0.8F, 0.8F, 1.0F));
        }
    }

    private static void renderToughnessBar(SpriteBatch sb, AbstractCreature c, float x, float y, float ratio, ToughnessPower power) {
        try {
            sb.setColor(new Color(0.1F, 0.1F, 0.1F, c.hbAlpha));

            // Draw the bar background
            sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, c.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_R, x + c.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

            // Draw the toughness bar from left to right
            if (power.amount <= 0) {
                sb.setColor(negativeColor.r, negativeColor.g, negativeColor.b, negativeColor.a * c.hbAlpha);
            } else if (power.getLocked()) {
                sb.setColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, Color.DARK_GRAY.a * c.hbAlpha);
            } else {
                sb.setColor(positiveColor.r, positiveColor.g, positiveColor.b, positiveColor.a * c.hbAlpha);
            }

            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ratio * c.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + ratio * c.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        } catch (Exception exception) {
            HSRMod.logger.error(exception.getMessage());
        }
    }
}