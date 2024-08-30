package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class TopazNumby2 extends BaseCard {
    public static final String ID = TopazNumby2.class.getSimpleName();
    
    public TopazNumby2() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Fire,
                        1,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && followedUp;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (c instanceof BaseCard) {
            BaseCard card = (BaseCard) c;
            if (card.followedUp && !followedUp) {
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }
}
