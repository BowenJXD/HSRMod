package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class March7th2 extends BaseCard {
    public static final String ID = March7th2.class.getSimpleName();
    
    boolean canBeUsed = false;

    public March7th2() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
            addToBot(new GainBlockAction(p, p, block));
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Ice,
                        1,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && (canBeUsed || followedUp);
    }

    @Override
    public void atTurnStartPreDraw() {
        canBeUsed = true;
        addToTop(new FollowUpAction(this));
    }
}
