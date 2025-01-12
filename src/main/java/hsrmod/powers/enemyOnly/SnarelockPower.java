package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.BasePower;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.breaks.ImprisonPower;

public class SnarelockPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SnarelockPower.class.getSimpleName());
    
    AbstractCreature source;
    
    public SnarelockPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, owner, amount);
        canGoNegative = true;
        isTurnBased = true;
        this.source = source;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        switch (amount) {
            case 1:
                this.description = DESCRIPTIONS[1];
                break;
            case -1:
                this.description = DESCRIPTIONS[2];
                break;
            default:
                this.description = DESCRIPTIONS[0];
                break;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (card instanceof BaseCard && ((BaseCard) card).followedUp) {
            return;
        } 
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount == 1)
                addToBot(new ApplyPowerAction(this.owner, this.owner, new ImprisonPower(this.owner, 1), 1));
            else
                switchMode();
        } else {
            if (amount == -1)
                addToBot(new ApplyPowerAction(this.owner, this.owner, new EntanglePower(this.owner, source, 1), 1));
            else
                switchMode();
        }
    }

    void switchMode() {
        if (amount > 0) {
            amount = -1;
        } else {
            amount = 1;
        }
        updateDescription();
    }
}
