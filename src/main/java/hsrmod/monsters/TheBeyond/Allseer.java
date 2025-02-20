package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.NightfallPower;

public class Allseer extends BaseMonster {
    public static final String ID = Allseer.class.getSimpleName();

    public Allseer(float x, float y) {
        super(ID, 140, 287, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? -1 : 1;
        
        addMoveA(Intent.ATTACK, 10, mi->{
            addToBot(new VFXAction(new SmallLaserEffect(this.hb.cX - this.hb.width / 2, this.hb.cY, p.hb.cX, p.hb.cY), 0.5F));
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            addToBot(new ApplyPowerAction(p, this, new NightfallPower(p, 1), 1));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
