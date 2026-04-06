package hsrmod.actions;

import com.megacrit.cardcrawl.actions.common.DarkOrbEvokeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class DarkEvokeToAction extends DarkOrbEvokeAction {
    public DarkEvokeToAction(AbstractOrb orb, AbstractCreature target) {
        super(new DamageInfo(AbstractDungeon.player, orb.evokeAmount, DamageInfo.DamageType.THORNS), AttackEffect.FIRE);
        this.target = target;
    }
}
