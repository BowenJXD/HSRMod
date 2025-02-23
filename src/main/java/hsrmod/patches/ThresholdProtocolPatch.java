package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.enemyOnly.SafeguardPower;

public class ThresholdProtocolPatch {    
    @SpirePatch(clz = AbstractMonster.class, method = "usePreBattleAction")
    public static class usePreBattleAction {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst) {
            if (AbstractDungeon.ascensionLevel >= 20) {
                if (HSRModConfig.tpThorn) {
                    _inst.addToBot(new ApplyPowerAction(_inst, _inst, new ThornsPower(_inst, 1), 1));
                }
                if (HSRModConfig.tpMalleable) {
                    _inst.addToBot(new ApplyPowerAction(_inst, _inst, new MalleablePower(_inst, 1), 1));
                }
                if (HSRModConfig.tpRitual) {
                    _inst.addToBot(new ApplyPowerAction(_inst, _inst, new RitualPower(_inst, 1, false), 1));
                }
                if (HSRModConfig.tpSafeguard 
                        && (_inst.type == AbstractMonster.EnemyType.ELITE || _inst.type == AbstractMonster.EnemyType.BOSS)) {
                    _inst.addToBot(new ApplyPowerAction(_inst, _inst, new SafeguardPower(_inst, 1)));
                }
                int count = HSRModConfig.getActiveTPCount();
                if (count > 0) {
                    AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(_inst, count / 5f, true));
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
}
