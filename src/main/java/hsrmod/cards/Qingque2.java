package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Havoc;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.ReduceECByHandCardNumAction;
import hsrmod.modcore.ElementType;
import hsrmod.powers.BreakEffectPower;
import hsrmod.powers.BrokenPower;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Qingque2 extends BaseCard {
    public static final String ID = Qingque2.class.getSimpleName();

    private int reduceCostProbability = 50;
    
    public Qingque2() {
        super(ID);
        this.tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new ElementalDamageAllAction(
                            damage,
                            ElementType.Quantum,
                            1,
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                            null
                    )
            );
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if (costForTurn == 0) return;
        if (AbstractDungeon.cardRandomRng.random(100) <= reduceCostProbability) {
            costForTurn--;
            if (costForTurn <= 0){
                AbstractDungeon.actionManager.addToBottom(new FollowUpAction(AbstractDungeon.player, this));
            }
        }
    }
}
