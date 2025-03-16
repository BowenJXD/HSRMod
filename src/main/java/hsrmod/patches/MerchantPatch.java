package hsrmod.patches;

import basemod.ReflectionHacks;
import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import hsrmod.modcore.HSRMod;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MerchantPatch {
    public static Texture handImg;
    public static Float scaleOverride = null;
    
    @SpirePatch(
            clz = Merchant.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {float.class, float.class, int.class}
    )
    public static class MerchantSkinPatch {
        @SpirePostfixPatch
        public static void postfix(Merchant _inst) {
            if (AbstractDungeon.id.contains("HSRMod:") || AbstractDungeon.id.equals("TheEnding")) {
                try {
                    scaleOverride = 0.4f;
                    _inst.anim = new AnimatedNpc((float)Settings.WIDTH * 0.5F + 200.0F * Settings.xScale, AbstractDungeon.floorY + 180.0F * Settings.scale, 
                            "HSRModResources/img/spine/daheita.atlas", "HSRModResources/img/spine/daheita.json", "idle");
                    scaleOverride = null;
                    
                    CharacterStrings merchantStrings = CardCrawlGame.languagePack.getCharacterString("HSRMod:HertaMerchant");
                    ArrayList<String> idleMessages = new ArrayList<>();
                    if (AbstractDungeon.id.equals("TheEnding")) {
                        Collections.addAll(idleMessages, merchantStrings.OPTIONS);
                    } else {
                        Collections.addAll(idleMessages, merchantStrings.TEXT);
                    }
                    ReflectionHacks.setPrivate(_inst, Merchant.class, "idleMessages", idleMessages);
                    
                } catch (Exception ignored) {
                    HSRMod.logger.log(Level.WARN, "Failed to load merchant skin");
                    scaleOverride = null;
                }
            }
        }
    }

    @SpirePatch(
            clz = ShopScreen.class,
            method = "init",
            paramtypez = {ArrayList.class, ArrayList.class}
    )
    public static class HeartShopUIPatch {
        public HeartShopUIPatch() {
        }

        @SpirePostfixPatch
        public static void Postfix(ShopScreen __result, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            if (AbstractDungeon.id.contains("HSRMod:") || AbstractDungeon.id.equals("TheEnding")) {
                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "handImg", handImg);
                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "HAND_W", 512 * Settings.scale);
                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "HAND_H", 1024 * Settings.scale);

                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "WELCOME_MSG", CardCrawlGame.languagePack.getCharacterString(HSRMod.makePath("ShopScreen")).NAMES[0]);
                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "NAMES", CardCrawlGame.languagePack.getCharacterString(HSRMod.makePath("ShopScreen")).NAMES);
                ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "TEXT", CardCrawlGame.languagePack.getCharacterString(HSRMod.makePath("ShopScreen")).TEXT);
                
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
    
    static {
        handImg = ImageMaster.loadImage("HSRModResources/img/UI/HertaHand.png");
    }
}

