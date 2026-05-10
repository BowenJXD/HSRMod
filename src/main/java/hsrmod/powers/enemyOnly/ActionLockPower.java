package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;

public class ActionLockPower extends StatePower {
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
        if (AbstractDungeon.isScreenUp || AbstractDungeon.actionManager.turnHasEnded) return;
        timer += Gdx.graphics.getDeltaTime();
        if (timer >  1) {
            timer -= 1;
            remove(1);
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        AbstractDungeon.topLevelEffects.add(new TopWarningEffect(GeneralUtil.tryFormat(DESCRIPTIONS[1], amount)));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (AbstractDungeon.overlayMenu.endTurnButton.enabled) {
            addToBot(new VFXAction(new TimeWarpTurnEndEffect()));
            addToBot(new PressEndTurnButtonAction());
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        addToBot(new VFXAction(new TimeWarpTurnEndEffect()));
        AbstractDungeon.topLevelEffects.add(new TopWarningEffect(GeneralUtil.tryFormat(DESCRIPTIONS[1], amount)));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
