/*
package hsrmod.patches;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import hsrmod.modcore.HSRMod;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;

public class MerchantPatch {
    
    @SpirePatch(
            clz = Merchant.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {float.class, float.class, int.class}
    )
    public static class MerchantSkinPatch {
        @SpirePostfixPatch
        public static void postfix(Merchant _inst) {
            if (AbstractDungeon.id.contains("HSRMod:")) {
                try {
                    _inst.anim = new AnimatedNpc((float)Settings.WIDTH * 0.5F + 34.0F * Settings.xScale, AbstractDungeon.floorY + 30.0F * Settings.scale, 
                            "HSRModResources/img/spine/daheita.atlas", "HSRModResources/img/spine/daheita.json", "idle");
                } catch (Exception ignored) {
                    HSRMod.logger.log(Level.WARN, "Failed to load merchant skin");
                }
            }
        }
    }
}

*/
