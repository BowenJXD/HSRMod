package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BoilPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SimmerPower;
import hsrmod.utils.ModHelper;

public class BeyondOvercooked extends BaseMonster {
    public static final String ID = BeyondOvercooked.class.getSimpleName();
    
    int simmerCount = 9;
    int burnCount = 1;
    
    public BeyondOvercooked(float x, float y) {
        super(ID, 300, 384, x, y);
        
        burnCount = moreDamageAs ? 2 : 1;
        
        addMove(Intent.ATTACK_BUFF, 10, mi -> {
            addToBot(new VFXAction(new RedFireballEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, 1), 0.3f));
            attack(mi, AbstractGameAction.AttackEffect.FIRE);
            addToBot(new ApplyPowerAction(this, this, new SimmerPower(this, simmerCount), 0));
        });
        addMove(Intent.ATTACK_DEBUFF, 20, mi -> {
            addToBot(new VFXAction(new RedFireballEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, 2), 0.3f));
            attack(mi, AbstractGameAction.AttackEffect.FIRE);
            AbstractCard card = new Burn();
            if (moreDamageAs) card.upgrade();
            addToBot(new MakeTempCardInDrawPileAction(card, burnCount, true, true));
        });
        addMove(Intent.ATTACK_BUFF, 30, mi -> {
            addToBot(new VFXAction(new RedFireballEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, 4), 0.3f));
            attack(mi, AbstractGameAction.AttackEffect.FIRE);
            if (hasPower(BoilPower.POWER_ID))
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        addMove(Intent.ATTACK, 40, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                addToBot(new VFXAction(new RedFireballEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, 9), 0.3f));
                attack(mi, AbstractGameAction.AttackEffect.FIRE);
            }
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (specialAs) {
            addToBot(new ApplyPowerAction(this, this, new SimmerPower(this, simmerCount), 0));
            addToBot(new RollMoveAction(this));
            ModHelper.addToBotAbstract(this::createIntent);
        }
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
        } else if (hasPower(BoilPower.POWER_ID)) {
            setMove(2);
        } else if (hasPower(SimmerPower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
