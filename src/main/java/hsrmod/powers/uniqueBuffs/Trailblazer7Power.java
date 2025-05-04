package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class Trailblazer7Power extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer7Power.class.getSimpleName());
    
    public Trailblazer7Power(AbstractCreature owner, int amount, boolean upgraded) {
        super(POWER_ID, owner, amount, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.flash();
            int amt = amount;
            if (upgraded) {
                int sum = 0;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    int i = m.currentBlock > 0 ? 1 : 0;
                    sum += i;
                }
                amt += sum;
            }
            this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amt, Settings.FAST_MODE));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
