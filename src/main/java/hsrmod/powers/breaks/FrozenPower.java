package hsrmod.powers.breaks;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.FrozenResistancePower;
import hsrmod.utils.ModHelper;

public class FrozenPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(FrozenPower.class.getSimpleName());
    
    private int amountRequired = 99;
    
    private boolean detecting = false;
    
    AbstractMonster monsterOwner;

    public FrozenPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        if (owner instanceof AbstractMonster) {
            monsterOwner = (AbstractMonster) owner;
        }
        amountRequired = getAmountRequired(monsterOwner);
        priority = 6;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amountRequired) + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        int frozenRes = ModHelper.getPowerCount(owner, FrozenResistancePower.POWER_ID);
        if (amount >= amountRequired + frozenRes){
            if (monsterOwner != null && monsterOwner.intent != AbstractMonster.Intent.STUN)
                addToBot(new StunMonsterAction((AbstractMonster) owner, AbstractDungeon.player));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new FrozenResistancePower(owner, 1), 1));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        detecting = true;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!detecting) return damageAmount;
        remove(1);
        return damageAmount;
    }

    public static int getAmountRequired(AbstractMonster m){
        int result = 99;
        if (m != null){
            switch (m.type){
                case NORMAL:
                    result = 1;
                    break;
                case ELITE:
                    result = 2;
                    break;
                case BOSS:
                    result = 3;
                    break;
            }
        }
        return result;
        
    }
}
