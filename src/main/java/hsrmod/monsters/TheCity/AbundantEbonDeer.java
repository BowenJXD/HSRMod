package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.LockToughnessAction;
import hsrmod.actions.UnlockToughnessAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.*;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class AbundantEbonDeer extends BaseMonster implements PostMonsterDeathSubscriber {
    public static final String ID = AbundantEbonDeer.class.getSimpleName();

    int spawnCount = 2, healPercentage = 10, regenCount = 4;
    boolean wintryWind, marpleLeaf, lavishFruits, gloriousBlooms;

    public AbundantEbonDeer() {
        super(ID, 270, 480, -150, 0);
        type = EnemyType.ELITE;
        bgm = "Deerstalker";

        spawnCount = specialAs ? 4 : 2;
        healPercentage = moreHPAs ? 12 : 10;

        addSlot(-550, 0);
        addSlot(-375, 30);
        addSlot(75, 30);
        addSlot(250, 0);
        monFunc = slot -> {
            if (!wintryWind) {
                wintryWind = true;
                return new TwigOfWintryWind(slot.x, slot.y);
            } else if (!marpleLeaf) {
                marpleLeaf = true;
                return new TwigOfMarpleLeaf(slot.x, slot.y);
            } else if (!lavishFruits) {
                lavishFruits = true;
                return new TwigOfLavishFruits(slot.x, slot.y);
            } else if (!gloriousBlooms) {
                gloriousBlooms = true;
                return new TwigOfGloriousBlooms(slot.x, slot.y);
            }
            return null;
        };

        addMove(Intent.UNKNOWN, mi -> {
            spawnMonsters(spawnCount);
            AbstractDungeon.getMonsters().monsters.stream()
                    .filter(m -> !Objects.equals(m.id, id) && ModHelper.check(m) && !m.hasPower(SummonedPower.POWER_ID))
                    .limit(spawnCount / 2)
                    .forEach(m -> addToBot(new ApplyPowerAction(m, this, new SummonedPower(m))));
        });
        addMoveA(Intent.ATTACK, 3, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        });
        addMove(Intent.DEFEND_BUFF, mi -> {
            int twigCount = AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> ModHelper.check(m) && !Objects.equals(m.id, id) ? 1 : 0).sum();
            addToBot(new HealAction(this, this, healPercentage * maxHealth / 100));
            addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, twigCount)));
        });
        addMove(Intent.BUFF, mi -> {
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            addToBot(new LockToughnessAction(this, name));
            SubscriptionManager.subscribe(this);
        });
        addMoveA(Intent.ATTACK, 6,
                () -> {
                    return (int) AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).count();
                },
                mi -> {
                    if (hasPower(ChargingPower.POWER_ID)) {
                        attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                        ModHelper.killAllMinions();
                        wintryWind = marpleLeaf = lavishFruits = gloriousBlooms = false;
                        addToBot(new UnlockToughnessAction(this, name));
                        addToBot(new RemoveSpecificPowerAction(this, this, LavishFruitPower.POWER_ID));
                        addToBot(new RemoveSpecificPowerAction(this, this, StrengthPower.POWER_ID));
                    }
                    SubscriptionManager.unsubscribe(this);
                });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new VigorOverflowPower(this, 1)));
        if (specialAs)
            addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 4)));
    }

    @Override
    protected void getMove(int i) {
        int spawnedCount = 0;
        if (getEmptySlotCount() == 4) {
            wintryWind = marpleLeaf = lavishFruits = gloriousBlooms = false;
        }
        if (wintryWind) spawnedCount++;
        if (marpleLeaf) spawnedCount++;
        if (lavishFruits) spawnedCount++;
        if (gloriousBlooms) spawnedCount++;
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(4);
        } else if (spawnedCount < 4) {
            setMove(0);
        } else if (ModHelper.getPowerCount(this, LavishFruitPower.POWER_ID) >= 2) {
            setMove(3);
        } else if (currentHealth < maxHealth / 2 && lastMove((byte) 2)) {
            setMove(2);
        } else {
            setMove(1);
        }
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this)
                && !Objects.equals(monster.id, id)
                && hasPower(ChargingPower.POWER_ID)) {
            setMove(4);
            createIntent();
        }
    }
}
