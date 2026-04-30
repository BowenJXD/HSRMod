package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Trailblaze.Demiurge2;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class AmphoreanLove extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(AmphoreanLove.class.getSimpleName());
    public static final int TRIGGER_THRESHOLD = 4;

    public AmphoreanLove(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = GeneralUtil.tryFormat(DESCRIPTIONS[0], TRIGGER_THRESHOLD);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        stackPower(1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        ModHelper.addToBotAbstract(() -> {
            if (isPlayer && amount >= TRIGGER_THRESHOLD) {
                flash();
                amount = 0;
                updateDescription();
                addToBot(new FollowUpAction(new Demiurge2()));
            }
        });
    }
}
