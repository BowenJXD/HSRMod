package hsrmod.relics.common;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class FruitOfTheAlienTree extends BaseRelic {
    public static final String ID = FruitOfTheAlienTree.class.getSimpleName();

    public FruitOfTheAlienTree() {
        super(ID);
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (AbstractDungeon.player.currentHealth - damageAmount <= 1 && !usedUp) {
            damageAmount = 0;
            onTrigger();
        }
        return damageAmount;
    }

    public void onTrigger() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        int healAmt = AbstractDungeon.player.maxHealth * magicNumber / 100;
        if (healAmt < 1) {
            healAmt = 1;
        }

        AbstractDungeon.player.heal(healAmt, true);
        destroy();
    }
}
