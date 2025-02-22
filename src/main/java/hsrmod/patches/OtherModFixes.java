package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.uncommon.Acheron1;
import hsrmod.cards.uncommon.Aventurine1;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.TheBeyond.AventurineOfStratagems;
import hsrmod.utils.ModHelper;
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
                    String[] extDesc = CardCrawlGame.languagePack.getCardStrings("HSRMod:Acheron1").EXTENDED_DESCRIPTION;
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV("SlashedDream1", 2));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, extDesc[0], 1, 2));
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            } else if (Objects.equals(card.cardID, "AcheronMod:AcheronCard")) {
                try {
                    String[] extDesc = CardCrawlGame.languagePack.getCardStrings("HSRMod:Acheron1").EXTENDED_DESCRIPTION;
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV("SlashedDream2", 2));
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
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV("AventurineOfStratagems_6", 1));
                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(AbstractDungeon.player, CardCrawlGame.languagePack.getMonsterStrings("HSRMod:AventurineOfStratagems").DIALOG[6]));
                } catch (Exception e) {
                    HSRMod.logger.log(Level.ERROR, e);
                }
            }
        }
    }
}
