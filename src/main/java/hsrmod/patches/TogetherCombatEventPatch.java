package hsrmod.patches;

import basemod.BaseMod;
import basemod.abstracts.events.PhasedEvent;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.SlaversCollar;
import com.megacrit.cardcrawl.ui.MultiPageFtue;

public class TogetherCombatEventPatch {
    @SpirePatch(clz = PhasedEvent.class, method = "enterCombat") 
    public static class EnterCombat {
        @SpirePostfixPatch
        public static void postFix(PhasedEvent __instance) {
            if (BaseMod.hasModID("spireTogether:")) {
                AbstractPlayer p = AbstractDungeon.player;
                /*p.drawPile.initializeDeck(p.masterDeck);
                p.energy.prep();
                p.healthBarUpdatedEvent();
                p.applyPreCombatLogic();*/

                if (!(Boolean) TipTracker.tips.get("COMBAT_TIP")) {
                    AbstractDungeon.ftue = new MultiPageFtue();
                    TipTracker.neverShowAgain("COMBAT_TIP");
                }

                AbstractDungeon.actionManager.clear();
                p.damagedThisCombat = 0;
                p.cardsPlayedThisTurn = 0;
                p.maxOrbs = 0;
                p.orbs.clear();
                p.increaseMaxOrbSlots(p.masterMaxOrbs, false);
                p.isBloodied = p.currentHealth <= p.maxHealth / 2;
                AbstractPlayer.poisonKillCount = 0;
                GameActionManager.playerHpLastTurn = p.currentHealth;
                p.endTurnQueued = false;
                p.gameHandSize = p.masterHandSize;
                p.isDraggingCard = false;
                p.isHoveringDropZone = false;
                p.hoveredCard = null;
                p.cardInUse = null;
                p.drawPile.initializeDeck(p.masterDeck);
                AbstractDungeon.overlayMenu.endTurnButton.enabled = false;
                p.hand.clear();
                p.discardPile.clear();
                p.exhaustPile.clear();
                if (AbstractDungeon.player.hasRelic("SlaversCollar")) {
                    ((SlaversCollar)AbstractDungeon.player.getRelic("SlaversCollar")).beforeEnergyPrep();
                }

                p.energy.prep();
                p.powers.clear();
                p.isEndingTurn = false;
                p.healthBarUpdatedEvent();
                if (ModHelper.isModEnabled("Lethality")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 3), 3));
                }

                if (ModHelper.isModEnabled("Terminal")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, 5), 5));
                }

                AbstractDungeon.getCurrRoom().monsters.usePreBattleAction();
                if (Settings.isFinalActAvailable && AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
                    AbstractDungeon.getCurrRoom().applyEmeraldEliteBuff();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(1.0F));
                p.applyPreCombatLogic();
            }
        }
    }
}
