package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlyingSpikeEffect;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.FormationCorePower;
import hsrmod.utils.ModHelper;

public class FlyingSword extends BaseMonster {
    public static final String ID = FlyingSword.class.getSimpleName();

    FormationCorePower.Tag tag;
    
    public FlyingSword(float x, float y, FormationCorePower.Tag tag) {
        super(ID, 100, 200, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;
        this.tag = tag;
        
        addMoveA(Intent.ATTACK_DEBUFF, 3, mi->{
            addToBot(new AnimateFastAttackAction(this));
            addToBot(new VFXAction(new FlyingSpikeEffect(hb.cX, hb.cY, 0, p.hb.cX - hb.cX, p.hb.cY - hb.cY, Color.CYAN)));
            addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, dmg->{
                if (dmg > 0) {
                    addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
                }
            }));
        });
    }
    
    public FlyingSword(float x, float y) {
        this(x, y, FormationCorePower.Tag.EXHAUST);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        boolean hidden = ModHelper.specialAscension(EnemyType.ELITE); // 
        addToBot(new ApplyPowerAction(this, this, new FormationCorePower(this, tag, false)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
