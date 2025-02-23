package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class PlaneshredClaws extends BaseMonster {
    public static final String ID = PlaneshredClaws.class.getSimpleName();
    
    public PlaneshredClaws(float x, float y) {
        super(ID, 128, 128, x, y);

        addMove(Intent.ATTACK, 10, mi -> {
            addToBot(new VFXAction(new ClawEffect(p.hb.cX, p.hb.cY, Color.WHITE, Color.SKY)));
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        });
        addMoveA(Intent.ATTACK, 20, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        });
        addMove(Intent.STUN, mi -> {});
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ToughnessPower.POWER_ID) && ((ToughnessPower) getPower(ToughnessPower.POWER_ID)).getLocked()) {
            setMove(2);
        } else if (hasPower(ResonatePower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void die() {
        AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof NebulaDevourer && !ModHelper.check(m)).forEach(m -> addToBot(new InstantKillAction(m)));
        super.die();
    }
}
