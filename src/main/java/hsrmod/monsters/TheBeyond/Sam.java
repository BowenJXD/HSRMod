package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import hsrmod.effects.MultiSlashEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.MoltenCorePower;
import hsrmod.powers.enemyOnly.SecondaryCombustionPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.utils.ModHelper;

public class Sam extends BaseMonster {
    public static final String ID = Sam.class.getSimpleName();
    
    int hpLoss = 10;
    int secondaryCombustionCount = 2;
    int moltenCoreCount = 10;
    int ultCount = 2;
    
    public Sam(){
        super(ID, 300, 420);
        bgm = "Nevermore";
        
        secondaryCombustionCount = specialAs ? 4 : 3;
        
        addMove(Intent.ATTACK_DEBUFF, 6, 3, mi->{
            shoutIf(0);
            addToBot(new AnimateFastAttackAction(this));
            for (int i = 0; i < mi.damageTimeSupplier.get(); i++) {
                addToBot(new VFXAction(new SearingBlowEffect(p.hb.cX, p.hb.cY, 1)));
                addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.FIRE, dmg->{
                    if (dmg > 0) {
                        Burn burn = new Burn();
                        if (moreDamageAs) burn.upgrade();
                        addToBot(new MakeTempCardInDrawPileAction(burn, 1, true, true));
                    }
                }));
            }
        });
        addMove(Intent.ATTACK_BUFF, 20, mi->{
            shoutIf(1);
            addToBot(new VFXAction(new VerticalImpactEffect(p.hb.cX, p.hb.cY), 0.6f));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        });
        addMove(Intent.UNKNOWN, mi->{
            shout(2);
            addToBot(new VFXAction(new ScreenOnFireEffect()));
            addToBot(new LoseHPAction(this, this, hpLoss));
            addToBot(new RemoveSpecificPowerAction(this, this, LockToughnessPower.POWER_ID));
            addToBot(new ApplyPowerAction(this, this, new SecondaryCombustionPower(this, secondaryCombustionCount)));
            addToBot(new ApplyPowerAction(this, this, new MoltenCorePower(this, moltenCoreCount)));
        });
        addMove(Intent.ATTACK_DEBUFF, 24, mi->{
            addToBot(new VFXAction(new VerticalImpactEffect(p.hb.cX, p.hb.cY), 0.6f));
            attack(mi, AbstractGameAction.AttackEffect.FIRE);
            Burn burn = new Burn();
            if (moreDamageAs) burn.upgrade();
            addToBot(new MakeTempCardInDrawPileAction(burn, 1, true, true));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, specialAs ? MOVES[6] : MOVES[5])));
            ultCount = 2;
        });
        addMoveA(Intent.ATTACK, 10, () -> ultCount, mi->{
            if (hasPower(ChargingPower.POWER_ID)) {
                shout(3, 4);
                int dmgTime = mi.damageTimeSupplier.get();
                addToBot(new VFXAction(new MultiSlashEffect(p.hb.cX, p.hb.cY, dmgTime, Color.FIREBRICK, Color.ORANGE), Settings.FAST_MODE ? 0.6f : 0f));
                if (specialAs) {
                    attack(mi, AbstractGameAction.AttackEffect.FIRE, AttackAnim.FAST);
                } else {
                    for (int i = 0; i < dmgTime; i++) {
                        addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.FIRE, dmg->{
                            if (dmg > 0) {
                                addToBot(new ApplyPowerAction(p, this, new BurnPower(p, this, 1)));
                            }
                        }));
                    }
                }
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, specialAs ? MOVES[6] : MOVES[5])));
                ultCount++;
            }
        });
        
        turnCount = specialAs ? 1 : 0;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new LockToughnessPower(this)));
    }

    @Override
    protected void getMove(int i) {
        turnCount++;
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(4);
        } else if (hasPower(SecondaryCombustionPower.POWER_ID)) {
            setMove(3);
        } else if (turnCount == 2) {
            setMove(2);
        } else if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 5, 2f));
    }
}
