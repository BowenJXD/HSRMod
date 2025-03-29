package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.enemyOnly.SafeguardPower;

import java.util.HashSet;

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
