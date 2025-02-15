package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class SafeguardPower extends BuffPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(SafeguardPower.class.getSimpleName());
    String normalImgUrl;
    String brokenImgUrl;

    int dmgReduce = 80;

    public SafeguardPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        normalImgUrl = SafeguardPower.class.getSimpleName();
        brokenImgUrl = "Safeguard_BrokenPower";
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], dmgReduce);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (ModHelper.getPowerCount(owner, ToughnessPower.POWER_ID) > 0
                && damageType == DamageInfo.DamageType.NORMAL) {
            return damage * dmgReduce / 100;
        }
        return damage;  
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof ToughnessPower) {
            int count = ModHelper.getPowerCount(target, ToughnessPower.POWER_ID);
            if (count > 0 != count + stackAmount > 0) {
                if (count > 0) loadRegion(brokenImgUrl);
                else loadRegion(normalImgUrl);
            }
        }
        return stackAmount;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar")
    public static class RenderHealthPatch {
        private static final float HEALTH_BAR_HEIGHT;
        private static final float HEALTH_BAR_OFFSET_Y;
        static Color guardColor;

        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __inst, SpriteBatch sb, float x, float y) {
            if (__inst.hasPower(POWER_ID) && ModHelper.getPowerCount(__inst, ToughnessPower.POWER_ID) > 0 && !Settings.hideCombatElements) {
                sb.setColor(guardColor);
                sb.setBlendFunction(770, 1);
                sb.draw(ImageMaster.BLOCK_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.BLOCK_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, __inst.hb.width, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.BLOCK_BAR_R, x + __inst.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                sb.setBlendFunction(770, 771);
            }
        }
        
        static {
            guardColor = new Color(1f, 1f, 1f, 0.5f);
            HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
            HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
        }
    }
}
