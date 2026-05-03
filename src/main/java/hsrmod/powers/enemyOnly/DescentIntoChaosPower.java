package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class DescentIntoChaosPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(DescentIntoChaosPower.class.getSimpleName());

    int count;
    
    public DescentIntoChaosPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount, amount);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        count = amount;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (count > 0) {
            count--;
            card.exhaustOnUseOnce = true;
        }
        ModHelper.addToBotAbstract(() -> {
            ModHelper.findCards(c -> c == card, true, true, true, false, true)
                    .forEach(r -> addToTop(new ExhaustSpecificCardAction(r.card, r.group)));
        });
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        AbstractDungeon.player.hand.group.stream().filter(c -> !c.selfRetain && !c.retain).limit(amount).forEach(c -> {
            addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
        });
    }
}
