package hsrmod.monsters.TheCity;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.utils.ModHelper;

public class IlluminationDragonfish extends BaseMonster {
    public static final String ID = IlluminationDragonfish.class.getSimpleName();
    
    int explosionDmg = 5;
    int vulnerableAmt = 1;
    
    public IlluminationDragonfish(float x, float y) {
        super(ID, 211, 240, x, y);
        
        setDamagesWithAscension(6);
        vulnerableAmt = ModHelper.specialAscension(type) ? 2 : 1;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDying && !m.isDead) {
                    addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(m, explosionDmg, DamageInfo.DamageType.THORNS, ElementType.Fire, 2), AbstractGameAction.AttackEffect.FIRE));
                    addToBot(new ApplyPowerAction(m, this, new VulnerablePower(m, 1, true), 1));
                }
            }
        })));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new AnimateFastAttackAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, vulnerableAmt, true), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }

    @Override
    public void update() {
        super.update();
        this.animX = MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }
}
