package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import hsrmod.actions.FollowUpAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;

public class OutragePower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(OutragePower.class.getSimpleName());
    
    public OutragePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        addToBot(new VFXAction(new VerticalAuraEffect(Color.RED, owner.hb.cX, owner.hb.cY)));
        ModHelper.addToBotAbstract(() -> {
            ArrayList<AbstractCard> attacks = AbstractDungeon.player.hand.getAttacks().group;
            for (int i = 0; i < amount && i < attacks.size(); i++) {
                addToTop(new FollowUpAction(attacks.get(i), null, false));
            }
        });
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
