package hsrmod.powers.enemyOnly;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.breaks.ImprisonPower;

public class SnarelockPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SnarelockPower.class.getSimpleName());
    
    AbstractCreature source;
    CardBorderGlowManager.GlowInfo glowInfo;
    
    public SnarelockPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, owner, amount);
        canGoNegative = true;
        isTurnBased = true;
        this.source = source;
        updateDescription();
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return SnarelockPower.this.amount != 0 
                        && (abstractCard.type == AbstractCard.CardType.ATTACK) == (SnarelockPower.this.amount == 1);
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.RED;
            }

            @Override
            public String glowID() {
                return POWER_ID;
            }
        };
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
    public void onInitialApplication() {
        super.onInitialApplication();
        CardBorderGlowManager.removeGlowInfo(POWER_ID);
        CardBorderGlowManager.addGlowInfo(glowInfo);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        CardBorderGlowManager.removeGlowInfo(glowInfo);
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
                switchMode(1);
        } else {
            if (amount == -1)
                addToBot(new ApplyPowerAction(this.owner, this.owner, new EntanglePower(this.owner, source, 1), 1));
            else
                switchMode(-1);
        }
    }

    void switchMode(int newAmount) {
        amount = newAmount;
        updateDescription();
    }
}
