package hsrmod.monsters.Exordium;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BanPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class GuardianShadow extends BaseMonster implements PreBreakSubscriber {
    public static final String ID = GuardianShadow.class.getSimpleName();
    
    public GuardianShadow() {
        super(ID, 0F, -15.0F, 384, 384, -100, 0);
        setDamagesWithAscension(8);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type)) {
            addToBot(new ApplyPowerAction(this, this, new BanPower(this)));
        }
        BaseMod.subscribe(this);
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new RemoveSpecificPowerAction(this, this, BanPower.POWER_ID));
        addToBot(new ApplyPowerAction(this, this, new BanPower(this)));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
    
    @Override
    public void update() {
        super.update();
        this.animY = MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == this
                && p.hasPower(BanPower.POWER_ID)) {
            addToTop(new RemoveSpecificPowerAction(p, this, BanPower.POWER_ID));
        }
    }
}
