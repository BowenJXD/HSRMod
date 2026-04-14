package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import java.util.List;
import java.util.Objects;

public class AddToDarkAction extends AbstractGameAction {
    List<AbstractOrb> orbs;
    
    public AddToDarkAction(List<AbstractOrb> orbs, int amount) {
        this.orbs = orbs;
        this.amount = amount;
    }
    
    public AddToDarkAction(int amount) {
        this(AbstractDungeon.player.orbs, amount);
    }

    @Override
    public void update() {
        isDone = true;
        for (AbstractOrb orb : orbs) {
            if (orb == null || orb instanceof EmptyOrbSlot || !Objects.equals(orb.ID, Dark.ORB_ID)) {
                continue;
            }
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.DARK), Settings.FAST_MODE ? 0.0F : 0.6F / orbs.size()));
            orb.evokeAmount += amount;
            orb.updateDescription();
        }
    }
}
