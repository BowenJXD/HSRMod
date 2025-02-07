package hsrmod.monsters.TheCity;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ReverberationPower;
import hsrmod.utils.ModHelper;

public class AurumatonSpectralEnvoy extends BaseMonster {
    public static final String ID = AurumatonSpectralEnvoy.class.getSimpleName();
    
    public AurumatonSpectralEnvoy(float x, float y) {
        super(ID, 250, 384, x, y);
        
        addMoveA(Intent.ATTACK_DEBUFF, 7, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
            addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
        });
        addMoveA(Intent.ATTACK_DEBUFF, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new ReverberationPower(p, 1)));
        });
        addMoveA(Intent.ATTACK, 10, mi -> {
            addToBot(new VFXAction(new ClashEffect(p.hb.cX, p.hb.cY), Settings.FAST_MODE ? 0.5f : 0.1f));
            addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.BLUNT_HEAVY, dmg -> {
                if (dmg > 0) {
                    addToTop(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
                }
            }));
        });
        addMove(Intent.ATTACK, 4, 4, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                shout(0, 1);
                attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
                addToBot(new RemoveSpecificPowerAction(p, this, ReverberationPower.POWER_ID));
            }
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type))
            turnCount = 2;
    }

    @Override
    protected void getMove(int i) {
        turnCount++;
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
        } else if (p.hasPower(ReverberationPower.POWER_ID)) {
            setMove(2);
        } else if (i <= 33 * turnCount){
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
