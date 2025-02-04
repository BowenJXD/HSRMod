package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.SpurOfThunderwoePower;
import hsrmod.utils.ModHelper;

public class InternalAlchemist extends BaseMonster {
    public static final String ID = InternalAlchemist.class.getSimpleName();
    
    int spurCount = 10;

    public InternalAlchemist(float x, float y) {
        super(ID, 170, 256, x, y + 40);
        
        spurCount = specialAs ? 12 : 10;
        
        addMove(Intent.DEBUFF, mi -> {
            addToBot(new ApplyPowerAction(p, this, new SpurOfThunderwoePower(p, spurCount)));
        });
        addMoveA(Intent.ATTACK, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        });
        
        floatIndex = -1;
    }

    @Override
    protected void getMove(int i) {
        if (p.hasPower(SpurOfThunderwoePower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void die() {
        int spurNum = ModHelper.getPowerCount(p, SpurOfThunderwoePower.POWER_ID);
        if (spurNum > 0) {
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(p.hb.cX, p.hb.cY));
            addToTop(new ElementalDamageAction(p, new ElementalDamageInfo(this, spurNum, DamageInfo.DamageType.THORNS, ElementType.None, 0), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new RemoveSpecificPowerAction(p, this, SpurOfThunderwoePower.POWER_ID));
        }
        super.die();
    }
}
