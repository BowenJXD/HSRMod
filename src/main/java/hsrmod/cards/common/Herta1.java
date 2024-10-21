package hsrmod.cards.common;

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
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Herta1 extends BaseCard {
    public static final String ID = Herta1.class.getSimpleName();

    private Map<String, Boolean> enemyHealthIsHalf;

    public Herta1() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        this.isMultiDamage = true;
        enemyHealthIsHalf = new HashMap<>();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
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
            ModHelper.addToBotAbstract(() -> {
                ModHelper.addToBotAbstract(() -> {
                    if (updateEnemyHealthIsHalf()) {
                        followedUp = true;
                        addToBot(new FollowUpAction(this));
                    }
                });
            });
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
}
