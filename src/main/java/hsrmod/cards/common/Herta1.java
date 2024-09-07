package hsrmod.cards.common;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

import java.util.HashMap;
import java.util.Map;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Herta1 extends BaseCard {
    public static final String ID = Herta1.class.getSimpleName();

    private Map<String, Boolean> enemyHealthIsHalf;

    public Herta1() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        enemyHealthIsHalf = new HashMap<>();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                        ElementType.Ice, 1,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
        );
    }

    @Override
    public void onEnterHand() {
        updateEnemyHealthIsHalf();
    }

    @Override
    public void triggerAtStartOfTurn() {
        updateEnemyHealthIsHalf();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (!followedUp) {
            followedUp = true;
            addToBot(new HertaAction(this));
        }
    }

    /**
     * @return true if changed
     */
    public boolean updateEnemyHealthIsHalf() {
        boolean result = false;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (enemyHealthIsHalf.containsKey(monster.id) && monster.isDeadOrEscaped()) {
                enemyHealthIsHalf.remove(monster.id);
            }

            boolean isHalf = monster.currentHealth <= monster.maxHealth / 2;
            if (enemyHealthIsHalf.containsKey(monster.id) && enemyHealthIsHalf.get(monster.id) != isHalf) {
                result = true;
            }
            enemyHealthIsHalf.put(monster.id, isHalf);
        }
        return result;
    }

    public static class HertaAction extends AbstractGameAction {
        Herta1 herta1;

        public HertaAction(Herta1 herta1) {
            this.actionType = ActionType.SPECIAL;
            this.herta1 = herta1;
        }

        @Override
        public void update() {
            if (!herta1.followedUp && herta1.updateEnemyHealthIsHalf()) {
                herta1.followedUp = true;
                addToBot(new FollowUpAction(herta1));
            }
            isDone = true;
        }
    }
}
