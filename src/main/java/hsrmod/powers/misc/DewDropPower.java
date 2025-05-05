package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.WaterDropEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DewDropPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(DewDropPower.class.getSimpleName());

    public DewDropPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        onSpecificTrigger();
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        AbstractMonster monster = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> ModHelper.check(m) && !m.hasPower(IntangiblePower.POWER_ID))
                .sorted((a, b) -> {
                    return Integer.compare(Math.abs(amount - a.currentHealth), Math.abs(amount - b.currentHealth));
                })
                .findAny()
                .orElse(ModHelper.betterGetRandomMonster());
        if (monster != null) {
            addToBot(new VFXAction(new WaterDropEffect(monster.hb.cX, monster.hb.cY)));
            addToBot(new DamageAction(monster, new DamageInfo(owner, amount)));
        }
    }
}
