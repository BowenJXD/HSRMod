package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.UnlockToughnessAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class AllOrNothing extends BaseMonster {
    public static final String ID = AllOrNothing.class.getSimpleName();

    public AllOrNothing(float x, float y) {
        super(ID, 180, 200, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;

        addMove(Intent.NONE, mi -> {
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], () -> {
            AbstractMonster aventurine = AbstractDungeon.getMonsters().monsters.stream().filter(monster -> monster instanceof AventurineOfStratagems).findFirst().orElse(null);
            if (aventurine != null) {
                addToTop(new RemoveSpecificPowerAction(aventurine, aventurine, IntangiblePower.POWER_ID));
                int tr = ModHelper.getPowerCount(aventurine, ToughnessPower.POWER_ID);
                addToBot(new UnlockToughnessAction(aventurine, aventurine));
                addToBot(new ElementalDamageAction(aventurine,
                        new ElementalDamageInfo(this, 0, DamageInfo.DamageType.HP_LOSS, ElementType.None, tr),
                        AbstractGameAction.AttackEffect.NONE)
                        .setIsSourceNullable(true)
                );
                int e = EnergyPower.AMOUNT_LIMIT - ModHelper.getPowerCount(p, EnergyPower.POWER_ID);
                addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, e), e));
            }
        })));
    }

    @Override
    public void damage(DamageInfo info) {
        int r = AbstractDungeon.aiRng.random(1, 6);
        DamageInfo info2 = new DamageInfo(info.owner, r, DamageInfo.DamageType.HP_LOSS);
        super.damage(info2);
    }
}
