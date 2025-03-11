package hsrmod.monsters.Exordium;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.GaugeRecollectionPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class DecayingShadow extends BaseMonster implements OnPowersModifiedSubscriber {
    public static final String ID = DecayingShadow.class.getSimpleName();

    public int block = 12;
    public int stack = 9;

    int gaugeAmount;

    public DecayingShadow(float x, float y) {
        super(ID, 0F, -15.0F, 300, 384, x, y);
        setDamagesWithAscension(5, 3);
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1)));
                break;
            case 1:
                addToBot(new GainBlockAction(this, block));
                addToBot(new ApplyPowerAction(this, this, new ToughnessPower(this), ToughnessPower.getStackLimit(this) * 2));
                break;
            case 2:
                addToBot(new ApplyPowerAction(this, this, new GaugeRecollectionPower(this, stack), stack));
                break;
            case 3:
                if (hasPower(GaugeRecollectionPower.POWER_ID)) {
                    addToBot(new AnimateSlowAttackAction(this));
                    addDamageActions(p, 1, ModHelper.getPowerCount(this, GaugeRecollectionPower.POWER_ID), AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                    addToBot(new RemoveSpecificPowerAction(this, this, GaugeRecollectionPower.POWER_ID));
                    BaseMod.unsubscribe(this);
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (turnCount == 0) {
            i = 0;
        }
        if (lastMove((byte) 2)) {
            BaseMod.subscribe(this);
            setMove(MOVES[3], (byte) 3, Intent.ATTACK, this.damage.get(1).base, ModHelper.getPowerCount(this, GaugeRecollectionPower.POWER_ID), true);
        } else if (turnCount == (ModHelper.specialAscension(type) ? 2 : 3)) {
            setMove(MOVES[2], (byte) 2, Intent.BUFF);
        } else {
            int r = i % 3;
            if (lastMove((byte) r)) {
                r = (r + 1) % 3;
            }
            switch (r) {
                case 0:
                    setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    break;
                case 1:
                    setMove(MOVES[1], (byte) 1, Intent.DEFEND_BUFF);
                    break;
                case 2:
                    setMove(MOVES[2], (byte) 2, Intent.BUFF);
                    break;
            }
        }
        turnCount++;
    }
    
    @Override
    public void update() {
        super.update();
        this.animY = MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    public void receivePowersModified() {
        if (SubscriptionManager.checkSubscriber(this)) {
            int amt = ModHelper.getPowerCount(this, GaugeRecollectionPower.POWER_ID);
            if (gaugeAmount != amt) {
                gaugeAmount = amt;
                if (gaugeAmount > 0) {
                    setMove(MOVES[3], (byte) 3, Intent.ATTACK, this.damage.get(1).base, amt, true);
                    createIntent();
                } else {
                    setMove(MOVES[3], (byte) 3, Intent.STUN);
                    createIntent();
                }
            }
        }
    }
}
