package hsrmod.powers.enemyOnly;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class GustoPower extends StatePower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(GustoPower.class.getSimpleName());

    CardBorderGlowManager.GlowInfo glowInfo;
    
    public GustoPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.ATTACK;
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
        SubscriptionManager.subscribe(this);
        CardBorderGlowManager.addGlowInfo(glowInfo);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        CardBorderGlowManager.removeGlowInfo(glowInfo);
        addToBot(new RemoveSpecificPowerAction(owner, owner, StrengthPower.POWER_ID));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (card.type == AbstractCard.CardType.ATTACK) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
        }
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }
}
