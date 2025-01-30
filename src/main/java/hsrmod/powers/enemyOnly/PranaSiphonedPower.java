package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class PranaSiphonedPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(PranaSiphonedPower.class.getSimpleName());

    public PranaSiphonedPower(AbstractCreature owner) {
        super(POWER_ID, owner, 1);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (card.type == AbstractCard.CardType.SKILL) {
            addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -1)));
            addToBot(new ApplyPowerAction(owner, owner, new PranaSiphonedPower(owner), 1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount)));
    }
}
