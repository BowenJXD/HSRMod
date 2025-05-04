package hsrmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import hsrmod.actions.ForceWaitAction;
import hsrmod.effects.GrayscaleScreenEffect;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.misc.VideoManager;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import org.apache.logging.log4j.Level;

import java.util.Objects;

public class OtherModFixes {
    
    /*@SpirePatch(clz = SoulStoneCustomSavable.class, method = "load", requiredModId = "RingOfDestiny")
    public static class RingOfDestinyFix {
        @SpirePrefixPatch 
        public static void prefix(@ByRef Integer[] integer) {
            if (integer[0] == null) integer[0] = 0;
        }
    }*/

    @SpirePatch(clz = AbstractPlayer.class, method = "playCard", requiredModId = "Acheron")
    public static class AcheronModEffect {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __inst) {
            AbstractCard card = __inst.hoveredCard;
            if (card == null) return;
            if (Objects.equals(card.cardID, "AcheronMod:CMJR")) {
                try {
                    AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Acheron"));
                    AbstractDungeon.topLevelEffects.add(new GrayscaleScreenEffect(Settings.FAST_MODE ? 2 : 3));
                    String[] extDesc = CardCrawlGame.languagePack.getCardStrings("HSRMod:Acheron1").EXTENDED_DESCRIPTION;
                    ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                        @Override
                        public void run() {
                            CardCrawlGame.sound.playV("SlashedDream1", 2);
                        }
                    });
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, extDesc[0], 1, 2));
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            } else if (Objects.equals(card.cardID, "AcheronMod:AcheronCard")) {
                try {
                    AbstractDungeon.topLevelEffects.add(new GrayscaleScreenEffect(Settings.FAST_MODE ? 2 : 3));
                    String[] extDesc = CardCrawlGame.languagePack.getCardStrings("HSRMod:Acheron1").EXTENDED_DESCRIPTION;
                    ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                        @Override
                        public void run() {
                            CardCrawlGame.sound.playV("SlashedDream2", 2);
                        }
                    });
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, extDesc[1], 1, 2));
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "playCard", requiredModId = "AventurineMod")
    public static class AventurineModEffect {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __inst) {
            AbstractCard card = __inst.hoveredCard;
            if (card == null) return;
            if (Objects.equals(card.cardID, "_22_Lunpanxunjue")) {
                try {
                    AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Aventurine"));
                    /*String[] extDesc = CardCrawlGame.languagePack.getCardStrings("HSRMod:Aventurine1").EXTENDED_DESCRIPTION;
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV("Aventurine1", 2));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, extDesc[0], 1, 2));*/
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            } else if (Objects.equals(card.cardID, "_52_AllOrNothing")) {
                try {
                    AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Aventurine"));
                    ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                        @Override
                        public void run() {
                            CardCrawlGame.sound.playV("AventurineOfStratagems_6", 1);
                        }
                    });
                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(AbstractDungeon.player, CardCrawlGame.languagePack.getMonsterStrings("HSRMod:AventurineOfStratagems").DIALOG[6]));
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            }
        }
    }
    
    public static void setRelics(){
        if (BaseMod.hasModID("AventurineMod:")) {
            String[] aEcoRelicIDs = new String[]{
                    "_01_SplitSilverCoin",
                    "_02_SplitGoldCoin",
                    "_03_Chunmei",
                    "_04_Heping",
                    "_53_TangibleLuck",
            };
            for (String id : aEcoRelicIDs) {
                RelicTagField.economic.set(RelicLibrary.getRelic(id), true);
            }
            String[] aSubtleRelicIDs = new String[]{
                    "_07_Fuhua",
                    "_15_TemporaryStake",
                    "_43_TheShackles",
                    "_46_Dreams0110",
                    "_13_Aha",
                    "_17_DignityAndPassion"
            };
            for (String id : aSubtleRelicIDs) {
                RelicTagField.subtle.set(RelicLibrary.getRelic(id), true);
            }
            String[] aDestRelicIDs = new String[]{
                    "_52_PaperFlower",
            };
            for (String id : aDestRelicIDs) {
                RelicTagField.destructible.set(RelicLibrary.getRelic(id), true);
            }
        }
    }
    
    /*@SpirePatch(clz = BaseMonster.class, method = "modifyTVByTogether", requiredModId = "spireTogether")
    public static class TogetherTVPatch {
        @SpirePrefixPatch
        public static SpireReturn<Integer> Prefix(int tv) {
            return SpireReturn.Return(tv + tv * P2PManager.GetPlayerCountWithoutSelf() * 50 / 100);
        }
    }*/
}
