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

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Qingque2 extends BaseCard {
    public static final String ID = Qingque2.class.getSimpleName();

    int costCache = -1;
    private int reduceCostProbability = 50;
    
    public Qingque2() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                            ElementType.Quantum, 1,
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
            );
        }
        setCostForTurn(costCache);
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        for (int i = 0; i < cardPlayed.costForTurn; i++) {
            if (AbstractDungeon.cardRandomRng.random(100) <= reduceCostProbability) {
                updateCost(-1);
                if (cost <= 0) {
                    addToBot(new FollowUpAction(this));
                    break;
                }
            }
        }
    }
}
