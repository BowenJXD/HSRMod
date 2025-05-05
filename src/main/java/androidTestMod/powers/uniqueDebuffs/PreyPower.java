package androidTestMod.powers.uniqueDebuffs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import androidTestMod.cardsV2.TheHunt.Moze2;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.DebuffPower;

public class PreyPower extends DebuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(PreyPower.class.getSimpleName());

    public int amount2Threshold = 3;
    public int amount2 = 0;
    public boolean canGoNegative2 = false;
    protected Color redColor2 = Color.RED.cpy();
    protected Color greenColor2 = Color.GREEN.cpy();
    
    public PreyPower(AbstractCreature creature, int amount) {
        super(POWER_ID, creature, amount);
        amount2 = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount2Threshold);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (card.type == AbstractCard.CardType.ATTACK 
                && !(card instanceof Moze2)
                && (action.target == owner || card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL)) {
            amount2++;
            if (amount2 >= amount2Threshold) {
                amount2 -= amount2Threshold;
                flash();
                remove(1);
                Moze2 moze2 = new Moze2();
                moze2.priorityTarget = owner;
                this.addToBot(new MakeTempCardInDrawPileAction(moze2, 1, false, true));
            }
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (this.amount2 > 0) {
            if (!this.isTurnBased) {
                this.greenColor2.a = c.a;
                c = this.greenColor2;
            }

            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15.0F * Settings.scale, this.fontScale, c);
        } else if (this.amount2 < 0 && this.canGoNegative2) {
            this.redColor2.a = c.a;
            c = this.redColor2;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15.0F * Settings.scale, this.fontScale, c);
        }
    }
}
