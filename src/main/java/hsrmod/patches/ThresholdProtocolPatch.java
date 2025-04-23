package hsrmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.enemyOnly.SafeguardPower;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ThresholdProtocolPatch {
    public static HashSet<AbstractMonster> processed = new HashSet<>();

    @SpirePatch(clz = AbstractMonster.class, method = "usePreBattleAction")
    public static class PreBattleActionPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst) {
            processMonster(_inst);
            if (processed != null) processed.add(_inst);
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "usePreBattleAction")
    public static class GroupPreBattlePatch {

        @SpirePrefixPatch
        public static void Prefix(MonsterGroup _inst) {
            processed = new HashSet<>();
        }

        @SpirePostfixPatch
        public static void Postfix(MonsterGroup _inst) {
            if (_inst.monsters != null) {
                for (AbstractMonster monster : _inst.monsters) {
                    if (processed.contains(monster)) continue;
                    processMonster(monster);
                }
            }
            processed = null;
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "render"
    )
    public static class RenderPrimeText {
        public RenderPrimeText() {
        }

        @SpireInsertPatch(
                rloc = 46
        )
        public static void Insert(SpriteBatch sb) {
            String str = (String)ReflectionHacks.getPrivateStatic(TipHelper.class, "HEADER");
            if (Objects.equals(str, CharacterSelectScreen.TEXT[8]) && HSRModConfig.getActiveTPCount() > 0) {
                try {
                    Class<?> clazz = TipHelper.class;
                    float bodyH = ReflectionHacks.getPrivateStatic(TipHelper.class, "textHeight");
                    ReflectionHacks.setPrivateStatic(TipHelper.class, "textHeight", 
                            30.0F * Settings.scale + 20.0F * Settings.scale * HSRModConfig.getActiveTPCount());
                    Method method = clazz.getDeclaredMethod("renderTipBox", Float.TYPE, Float.TYPE, SpriteBatch.class, String.class, String.class);
                    method.setAccessible(true);
                    method.invoke((Object)null, (float)InputHelper.mX + 50.0F * Settings.scale + 320.0F * Settings.scale, 
                            (float)Settings.HEIGHT - 120.0F * Settings.scale, sb, HSRModConfig.getHeader(), HSRModConfig.getTip());
                    ReflectionHacks.setPrivateStatic(TipHelper.class, "textHeight", bodyH);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    @SpirePatch(
            clz = TopPanel.class,
            method = "renderDungeonInfo"
    )
    public static class PatchRenderDungeonInfo {
        public PatchRenderDungeonInfo() {
        }

        @SpireInsertPatch(
                rloc = 11
        )
        public static SpireReturn<Void> Insert(TopPanel topPanel, SpriteBatch sb) {
            if (HSRModConfig.getActiveTPCount() > 0) {
                float floorX = (Float)ReflectionHacks.getPrivateStatic(TopPanel.class, "floorX");
                float ICON_Y = (Float)ReflectionHacks.getPrivateStatic(TopPanel.class, "ICON_Y");
                float ICON_W = (Float)ReflectionHacks.getPrivateStatic(TopPanel.class, "ICON_W");
                float INFO_TEXT_Y = (Float)ReflectionHacks.getPrivateStatic(TopPanel.class, "INFO_TEXT_Y");
                sb.draw(HSRModConfig.tpIcon, floorX + 120.0F * Settings.scale, ICON_Y, ICON_W, ICON_W);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, 
                        HSRModConfig.getHeader() + " Lv." + HSRModConfig.getActiveTPCount(), 
                        floorX + 166.0F * Settings.scale, INFO_TEXT_Y, Settings.GOLD_COLOR);
                if (topPanel.ascensionHb != null) {
                    topPanel.ascensionHb.render(sb);
                }
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    public static void processMonster(AbstractMonster m) {
        if (AbstractDungeon.ascensionLevel >= 20) {
            if (HSRModConfig.tpThorn) {
                m.addToBot(new ApplyPowerAction(m, m, new ThornsPower(m, 1), 1));
            }
            if (HSRModConfig.tpMalleable) {
                m.addToBot(new ApplyPowerAction(m, m, new MalleablePower(m, 1), 1));
            }
            if (HSRModConfig.tpRitual) {
                m.addToBot(new ApplyPowerAction(m, m, new RitualPower(m, 1, false), 1));
            }
            if (HSRModConfig.tpSafeguard
                    && (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS)) {
                m.addToBot(new ApplyPowerAction(m, m, new SafeguardPower(m, 1)));
            }
            int count = HSRModConfig.getActiveTPCount();
            if (count > 0) {
                AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, HSRModConfig.getHPInc(), true));
                if (AbstractDungeon.topLevelEffectsQueue.stream().noneMatch(e -> e instanceof TopWarningEffect)) {
                    if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
                        AbstractDungeon.topLevelEffectsQueue.add(new TopWarningEffect("⚠阈值协议生效⚠"));
                    } else {
                        AbstractDungeon.topLevelEffectsQueue.add(new TopWarningEffect("⚠THRESHOLD PROTOCOLS ACTIVATED⚠"));
                    }
                }
            }
        }
    }
}
