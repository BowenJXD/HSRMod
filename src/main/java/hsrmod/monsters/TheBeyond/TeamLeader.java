package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.InversionBeamEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.PerformancePointPower;
import hsrmod.utils.ModHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class TeamLeader extends BaseMonster {
    public static final String ID = TeamLeader.class.getSimpleName();

    int pointCount = 3;
    int pointThreshold = 5;
    int ultCount;

    public TeamLeader(float x, float y) {
        super(ID, 250, 384, x, y);

        ultCount = moreDamageAs ? 4 : 3;
        pointThreshold = specialAs ? 4 : 5;

        addSlot(x - 300, y + AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(x + 300, y + AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = slot -> new FieldPersonnel(slot.x, slot.y);

        addMove(Intent.UNKNOWN, mi -> {
            spawnMonsters();
            AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new PerformancePointPower(m, 1)));
            });
        });
        addMove(Intent.ATTACK, 5, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(this, this, new PerformancePointPower(this, pointCount)));
        });
        addMove(Intent.BUFF, mi -> {
            AtomicInteger point = new AtomicInteger();
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> ModHelper.check(m) && m != this).forEach(m -> {
                addToBot(new RemoveSpecificPowerAction(m, this, PerformancePointPower.POWER_ID));
                point.addAndGet(ModHelper.getPowerCount(m, PerformancePointPower.POWER_ID));
            });
            if (specialAs) {
                point.addAndGet(ModHelper.getPowerCount(p, PerformancePointPower.POWER_ID));
                addToBot(new RemoveSpecificPowerAction(p, this, PerformancePointPower.POWER_ID));
            }
            addToBot(new ApplyPowerAction(this, this, new PerformancePointPower(this, point.get())));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        addMove(Intent.ATTACK, 5, 3, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                addToBot(new VFXAction(new InversionBeamEffect(p.hb.cX)));
                attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                addToBot(new RemoveSpecificPowerAction(this, this, PerformancePointPower.POWER_ID));
            }
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (specialAs) {
            spawnMonsters();
            AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
                addToTop(new ApplyPowerAction(m, this, new PerformancePointPower(m, 1)));
            });
            rollMove();
            createIntent();
        }
    }

    @Override
    protected void getMove(int i) {
        int totalPoint = AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).mapToInt(m -> ModHelper.getPowerCount(m, PerformancePointPower.POWER_ID)).sum();
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
        } else if (totalPoint > pointThreshold) {
            setMove(2);
        } else if (getEmptySlotCount() < 2) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
