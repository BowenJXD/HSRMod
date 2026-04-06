package hsrmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;

import java.util.Objects;

public class TriggerLightningPassiveToAction extends AbstractGameAction {
    AbstractOrb orb;
    ElementalDamageInfo info;
    
    public TriggerLightningPassiveToAction(AbstractOrb orb, AbstractCreature target, int amount) {
        this.orb = orb;
        this.target = target;
        this.amount = amount;
        this.info = new ElementalDamageInfo(AbstractDungeon.player, orb.passiveAmount, DamageInfo.DamageType.THORNS, ElementType.Lightning, 1);
    }

    @Override
    public void update() {
        isDone = true;
        if (orb == null || target == null) return;
        for (int i = 0; i < amount; i++) {
            float speedTime = 0.2F / (float)AbstractDungeon.player.orbs.size();
            if (Settings.FAST_MODE) {
                speedTime = 0.0F;
            }

            this.info.output = AbstractOrb.applyLockOn(target, this.info.base);
            this.addToTop(new ElementalDamageAction(target, this.info, AttackEffect.NONE));
            this.addToTop(new VFXAction(new LightningEffect(target.drawX, target.drawY), speedTime));
            this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
            if (this.orb != null) {
                this.addToTop(new VFXAction(new OrbFlareEffect(this.orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
            }
        }
    }
}
