package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

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
        ModHelper.addToBotAbstract(() -> updateCost(costCache - costForTurn));
    }

    @Override
    public void updateCost(int amt) {
        super.updateCost(amt);
        followUp();
    }

    @Override
    public void modifyCostForCombat(int amt) {
        super.modifyCostForCombat(amt);
        followUp();
    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);
        followUp();
    }

    void followUp(){
        if (!followedUp && costForTurn == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        int loop = c.costForTurn;
        if (c.costForTurn == -1) loop = c.energyOnUse;
        for (int i = 0; i < loop; i++) {
            if (AbstractDungeon.cardRandomRng.random(100) <= reduceCostProbability) {
                updateCost(-1);
                if (costForTurn <= 0) break;
            }
        }
    }
}
