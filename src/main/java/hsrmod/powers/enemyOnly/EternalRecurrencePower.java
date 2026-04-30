package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class EternalRecurrencePower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(EternalRecurrencePower.class.getSimpleName());

    public EternalRecurrencePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
        loadRegion("heartDef");
    }

    @Override
    public void onDeath() {
        super.onDeath();
        ModHelper.addToBotAbstract(() -> {
                    addToTop(new DrawCardAction(BaseMod.MAX_HAND_SIZE));
                    addToTop(new ExhaustToHandAction(GeneralUtil.getRandomElement(AbstractDungeon.player.exhaustPile.group, AbstractDungeon.cardRandomRng)));
                }
        );
    }
}
