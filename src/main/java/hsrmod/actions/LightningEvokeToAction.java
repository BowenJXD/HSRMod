package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;

public class LightningEvokeToAction extends AbstractGameAction {
    AbstractOrb orb;
    ElementalDamageInfo info;
    
    public LightningEvokeToAction(AbstractOrb orb, AbstractCreature target, int amount) {
        this.orb = orb;
        this.target = target;
        this.amount = amount;
        this.info = new ElementalDamageInfo(AbstractDungeon.player, orb.evokeAmount, DamageInfo.DamageType.THORNS, ElementType.Lightning, 1);
    }

    @Override
    public void update() {
        isDone = true;
        if (orb == null || target == null) return;

        for (int i = amount; i > 0; i--) {
            float speedTime = 0.1F;
            if (!AbstractDungeon.player.orbs.isEmpty()) {
                speedTime = 0.2F / (float) AbstractDungeon.player.orbs.size();
            }

            if (Settings.FAST_MODE) {
                speedTime = 0.0F;
            }

            this.info.output = AbstractOrb.applyLockOn(target, this.info.base);
            this.addToTop(new DamageAction(target, this.info, AttackEffect.NONE, true));
            this.addToTop(new VFXAction(new LightningEffect(target.drawX, target.drawY), speedTime));
            this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }
    }
}
