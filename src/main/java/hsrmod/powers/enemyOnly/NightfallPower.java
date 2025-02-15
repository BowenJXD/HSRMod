package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class NightfallPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(NightfallPower.class.getSimpleName());
    
    public NightfallPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        card.exhaust = true;
        remove(1);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if (!usedCard.exhaust) {
            addToBot(new ExhaustSpecificCardAction(usedCard, AbstractDungeon.player.discardPile));
            remove(1);
        }
    }
}
