package hsrmod.powers.enemyOnly;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class PranaSiphonedPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(PranaSiphonedPower.class.getSimpleName());

    CardBorderGlowManager.GlowInfo glowInfo;
    
    public PranaSiphonedPower(AbstractCreature owner) {
        super(POWER_ID, owner, 1);
        updateDescription();
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.SKILL;
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
        description = String.format(DESCRIPTIONS[0], amount);
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

    @Override
    public void onDeath() {
        super.onDeath();
        CardBorderGlowManager.removeGlowInfo(glowInfo);
    }
}
