package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.relics.interfaces.ClickableRelic;

public class RottingFruitOfTheAlienTree extends BaseRelic implements ClickableRelic {
    public static final String ID = RottingFruitOfTheAlienTree.class.getSimpleName();

    int hpLoss = 3;
    
    public RottingFruitOfTheAlienTree() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void onRightClick() {
        if (usedUp) return;
        if (AbstractDungeon.currMapNode != null 
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead())
            onTrigger();
    }

    @Override
    public void onTrigger() {
        super.onTrigger();
        flash();
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, hpLoss));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, 1)));
        reduceCounterAndCheckDestroy();
    }
}
