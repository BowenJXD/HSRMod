package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;

public class ActionLockPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(ActionLockPower.class.getSimpleName());
    
    float timer;

    public ActionLockPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (AbstractDungeon.isScreenUp) return;
        timer += Gdx.graphics.getDeltaTime();
        if (timer >  1) {
            timer -= 1;
            remove(1);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        addToBot(new PressEndTurnButtonAction());
    }
}
