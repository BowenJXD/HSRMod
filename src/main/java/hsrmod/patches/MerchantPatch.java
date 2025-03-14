package hsrmod.patches;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import hsrmod.modcore.HSRMod;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class MerchantPatch {
    
    public static Float scaleOverride = null;
    
    @SpirePatch(
            clz = Merchant.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {float.class, float.class, int.class}
    )
    public static class MerchantSkinPatch {
        @SpirePostfixPatch
        public static void postfix(Merchant _inst/*, @ByRef ArrayList<String>[] ___idleMessages*/) {
            if (AbstractDungeon.id.contains("HSRMod:")) {
                try {
                    scaleOverride = 0.4f;
                    _inst.anim = new AnimatedNpc((float)Settings.WIDTH * 0.5F + 200.0F * Settings.xScale, AbstractDungeon.floorY + 180.0F * Settings.scale, 
                            "HSRModResources/img/spine/daheita.atlas", "HSRModResources/img/spine/daheita.json", "idle");
                    scaleOverride = null;
                    CharacterStrings merchantStrings = CardCrawlGame.languagePack.getCharacterString("HSRMod:HertaMerchant");
                    /*___idleMessages[0].clear();
                    if (AbstractDungeon.id.equals("TheEnding")) {
                        Collections.addAll(___idleMessages[0], merchantStrings.OPTIONS);
                    } else {
                        Collections.addAll(___idleMessages[0], merchantStrings.TEXT);
                    }*/
                } catch (Exception ignored) {
                    HSRMod.logger.log(Level.WARN, "Failed to load merchant skin");
                }
            }
        }
    }
    
    @SpirePatch(clz = SkeletonJson.class, method = "setScale")
    public static class SkeletonJsonPatch {
        @SpirePrefixPatch
        public static void prefix(SkeletonJson _inst, @ByRef float[] scale) {
            if (MerchantPatch.scaleOverride != null) {
                scale[0] = MerchantPatch.scaleOverride;
            }
        }
    }
}

