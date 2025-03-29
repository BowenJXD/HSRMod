package hsrmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.spine.SkeletonJson;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.common.FaithBond;
import hsrmod.utils.ModHelper;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Collections;

public class MerchantPatch {
    public static Texture hertaHand;
    public static Texture sparkleHand;
    public static Float scaleOverride = null;
    static MerchantChar merchantChar;
    public static final int SPARKLE_CHANCE = 1;
    
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
                    String atlasPath, jsonPath, charString;
                    if (AbstractDungeon.merchantRng.random(99) < SPARKLE_CHANCE) {
                        merchantChar = MerchantChar.SPARKLE;
                        atlasPath = "HSRModResources/img/spine/huahuo.atlas";
                        jsonPath = "HSRModResources/img/spine/huahuo.json";
                        charString = HSRMod.makePath("SparkleMerchant");
                    } else {
                        merchantChar = MerchantChar.HERTA;
                        atlasPath = "HSRModResources/img/spine/daheita.atlas";
                        jsonPath = "HSRModResources/img/spine/daheita.json";
                        charString = HSRMod.makePath("HertaMerchant");
                    }
                    
                    scaleOverride = 0.4f;
                    _inst.anim = new AnimatedNpc((float)Settings.WIDTH * 0.5F + 200.0F * Settings.xScale, AbstractDungeon.floorY + 180.0F * Settings.scale, 
                            atlasPath, jsonPath, "idle");
                    scaleOverride = null;
                    
                    CharacterStrings merchantStrings = CardCrawlGame.languagePack.getCharacterString(charString);
                    ArrayList<String> idleMessages = new ArrayList<>();
                    if (AbstractDungeon.id.equals("TheEnding")) {
                        Collections.addAll(idleMessages, merchantStrings.OPTIONS);
                    } else {
                        Collections.addAll(idleMessages, merchantStrings.TEXT);
                    }
                    ReflectionHacks.setPrivate(_inst, Merchant.class, "idleMessages", idleMessages);
                    ReflectionHacks.setPrivateStaticFinal(Merchant.class, "TEXT", merchantStrings.TEXT);
                    ReflectionHacks.setPrivateStaticFinal(Merchant.class, "ENDING_TEXT", merchantStrings.OPTIONS);

                    Texture hand = merchantChar == MerchantChar.HERTA ? hertaHand : sparkleHand;
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "handImg", hand);
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "HAND_W", 512 * Settings.scale);
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "HAND_H", 1024 * Settings.scale);

                    CharacterStrings shopString = CardCrawlGame.languagePack.getCharacterString(HSRMod.makePath(merchantChar == MerchantChar.HERTA ? "HertaShopScreen" : "SparkleShopScreen"));
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "WELCOME_MSG", shopString.NAMES[0]);
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "NAMES", shopString.NAMES);
                    ReflectionHacks.setPrivateStaticFinal(ShopScreen.class, "TEXT", shopString.TEXT);
                    if (!AbstractDungeon.id.equals("TheEnding"))
                        ReflectionHacks.setPrivate(AbstractDungeon.shopScreen, ShopScreen.class, "idleMessages", shopString.TEXT);

                    if (merchantChar == MerchantChar.SPARKLE) {
                        ModHelper.addEffectAbstract(() -> {
                            CardCrawlGame.music.silenceTempBgmInstantly();
                            AbstractDungeon.getCurrRoom().playBGM("Ensemble Cast");
                        });
                        AbstractDungeon.shopScreen.applyDiscount(0.1F, true);
                    }
                    
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
    public static class CustomShopUIPatch {
        public CustomShopUIPatch() {
        }

        @SpirePostfixPatch
        public static void postfix(ShopScreen _inst, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards, ArrayList<StoreRelic> ___relics) {
            if (ModHelper.hasRelic(FaithBond.ID)) {
                ___relics.forEach(r -> r.price = (int)(r.price * (1f - FaithBond.DISCOUNT / 100f)));
            }
        }
    }
    
    @SpirePatch(clz = ShopScreen.class, method = "getNewPrice", paramtypez = {StoreRelic.class})
    public static class ShopRelicPatch {
        @SpirePrefixPatch
        public static void prefix(ShopScreen _inst, StoreRelic r) {
            if (ModHelper.hasRelic(FaithBond.ID)) {
                r.price = (int)(r.price * (1f - FaithBond.DISCOUNT / 100f));
            }
        }
    }
    
    @SpirePatch(clz = SkeletonJson.class, method = "setScale")
    public static class SkeletonJsonPatch {
        @SpirePrefixPatch
        public static void prefix(SkeletonJson _inst, @ByRef float[] scale) {
            if (MerchantPatch.scaleOverride != null) {
                scale[0] = MerchantPatch.scaleOverride;
                MerchantPatch.scaleOverride = null;
            }
        }
    }
    
    enum MerchantChar {
        HERTA,
        SPARKLE,
    }
    
    static {
        hertaHand = ImageMaster.loadImage("HSRModResources/img/UI/HertaHand.png");
        sparkleHand = ImageMaster.loadImage("HSRModResources/img/UI/SparkleHand.png");
    }
}

