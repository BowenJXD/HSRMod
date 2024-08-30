package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class JingYuan1 extends BaseCard {
    public static final String ID = JingYuan1.class.getSimpleName();
    
    public JingYuan1() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new ElementalDamageAction(
                            m,
                            new DamageInfo(
                                    p,
                                    damage - energyOnUse,
                                    damageTypeForTurn
                            ),
                            ElementType.Lightning,
                            1,
                            // 伤害类型
                            AbstractGameAction.AttackEffect.SLASH_HEAVY
                    )
            );
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this) || followedUp) return;
        updateCost(-1);
        if (cost == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
