package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class SporePower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SporePower.class.getSimpleName());
    
    public int stackLimit = 6;
    
    public SporePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.amount = Math.min(amount, stackLimit);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], stackLimit, amount * amount);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= stackLimit) {
            amount = stackLimit;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (card.type == AbstractCard.CardType.ATTACK 
                && (m == owner || card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL)) {
            trigger();
        }
    }
    
    public void trigger(){
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        addToBot(new ElementalDamageAction(owner, new ElementalDamageInfo(AbstractDungeon.player, amount * amount, 
                DamageInfo.DamageType.NORMAL, ElementType.Wind, 1), AbstractGameAction.AttackEffect.POISON));
        addToBot(new ApplyPowerAction(owner, AbstractDungeon.player, new SporePower(owner, -amount), -amount));
    }
}
