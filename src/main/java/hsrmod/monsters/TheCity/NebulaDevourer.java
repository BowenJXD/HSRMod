package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.OutragePower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class NebulaDevourer extends BaseMonster {
    public static final String ID = NebulaDevourer.class.getSimpleName();
    
    public NebulaDevourer(float x, float y) {
        super(ID, 200, 256, x, y);
        floatIndex = 0.5f;
        
        addMove(Intent.ATTACK, 8, mi -> {
            addToBot(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY, Color.SKY)));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        });
        addMoveA(Intent.ATTACK_DEBUFF, 16, mi -> {
            addToBot(new VFXAction(new FireballEffect(hb.cX, hb.cY, p.hb.cX, p.hb.cY), 0.5f));
            addToBot(new VFXAction(new GhostIgniteEffect(p.hb.cX, p.hb.cY)));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            addToBot(new ApplyPowerAction(p, this, new OutragePower(p, 1)));
        });
        addMove(Intent.STUN, mi -> {});
    }

    @Override
    protected void getMove(int i) {
        if (halfDead || (hasPower(ToughnessPower.POWER_ID) && hasPower(LockToughnessPower.POWER_ID))) {
            setMove(2);
        } else if (hasPower(ResonatePower.POWER_ID)) {
            setMove(1);
        } else { 
            setMove(0);
        }
    }

    @Override
    public void die() {
        if (halfDead) {
            isDying = true;
        } else {
            super.die();
        }
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m instanceof PlaneshredClaws && ModHelper.check(m))) {
            isDying = false;
            halfDead = true;
            addToBot(new RollMoveAction(this));
            ModHelper.addToBotAbstract(this::createIntent);
        }
    }
}
