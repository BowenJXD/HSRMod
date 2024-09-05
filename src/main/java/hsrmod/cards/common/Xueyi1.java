package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.CustomEnums;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

public class Xueyi1 extends BaseCard {
    public static final String ID = Xueyi1.class.getSimpleName();

    private Map<String, Integer> enemyToughness;
    
    int costCache;
    
    public Xueyi1() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
        enemyToughness = new HashMap<>();
        costCache = cost;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = ModHelper.getPowerCount(mo, ToughnessPower.POWER_ID);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        // find the monster with greatest toughness
        AbstractMonster mo = AbstractDungeon.getMonsters().monsters.stream()
                .filter(monster -> !monster.isDead && !monster.isDying)
                .max((monster1, monster2) -> {
                    AbstractPower power1 = monster1.getPower(ToughnessPower.POWER_ID);
                    AbstractPower power2 = monster2.getPower(ToughnessPower.POWER_ID);
                    if (power1 == null && power2 == null) return 0;
                    if (power1 == null) return -1;
                    if (power2 == null) return 1;
                    return power1.amount - power2.amount;
                }).orElse(null);
                
        AbstractPower power = mo.getPower(ToughnessPower.POWER_ID);
        if (power == null) return;
        
        int toughnessReduction = upgraded ? power.amount : 3;
        
        addToBot(new ElementalDamageAction(
                mo,
                new DamageInfo(
                        p,
                        damage,
                        damageTypeForTurn
                ),
                ElementType.Quantum,
                toughnessReduction,
                // 伤害类型
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        modifyCostForCombat(costCache);
    }

    @Override
    public void triggerWhenDrawn() {
        updateEnemyToughness();
    }

    @Override
    public void triggerAtStartOfTurn() {
        updateEnemyToughness();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (!followedUp)
            addToBot(new Xueyi1Action(this, 1));
    }
    
    int updateEnemyToughness() {
        int result = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (enemyToughness.containsKey(monster.id) && monster.isDeadOrEscaped()) {
                enemyToughness.remove(monster.id);
            }

            AbstractPower power = monster.getPower(ToughnessPower.POWER_ID);
            int toughness = power == null ? 0 : power.amount;
            if (enemyToughness.containsKey(monster.id) && enemyToughness.get(monster.id) > toughness) {
                result += enemyToughness.get(monster.id) - toughness;
            }
            enemyToughness.put(monster.id, toughness);
        }
        return result;
    }

    public static class Xueyi1Action extends AbstractGameAction {
        Xueyi1 xueyi1;
        
        int waitStep;

        public Xueyi1Action(Xueyi1 xueyi1, int waitStep) {
            this.actionType = ActionType.SPECIAL;
            this.xueyi1 = xueyi1;
            this.waitStep = waitStep;
        }

        @Override
        public void update() {
            if (waitStep > 0) {
                waitStep--;
                isDone = true;
                addToBot(new Xueyi1Action(xueyi1, waitStep));
                return;
            }
            
            int toughnessReduction = xueyi1.updateEnemyToughness();
            if (toughnessReduction > 0) {
                xueyi1.modifyCostForCombat(-toughnessReduction);
                if (!xueyi1.followedUp && xueyi1.cost <= 0){
                    xueyi1.followedUp = true;
                    addToBot(new FollowUpAction(xueyi1));
                }
            }
            isDone = true;
        }
    }
}
