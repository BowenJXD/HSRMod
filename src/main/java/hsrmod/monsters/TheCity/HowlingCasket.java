package hsrmod.monsters.TheCity;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.MoonRagePower;
import hsrmod.powers.enemyOnly.TerrorPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class HowlingCasket extends BaseMonster implements OnPowersModifiedSubscriber {
    public static final String ID = HowlingCasket.class.getSimpleName();

    public HowlingCasket(float x, float y) {
        super(ID, 340, 384, x, y);
        
        addMoveA(Intent.ATTACK, 5, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
        });
        addMove(Intent.BUFF, mi -> {
            shoutIf(1);
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> ModHelper.check(m) && !m.hasPower(MoonRagePower.POWER_ID)).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new MoonRagePower(m, 1)));
                addToBot(new LoseHPAction(this, this, 4));
            });
        });
        addMoveA(Intent.ATTACK_DEBUFF, 3, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AttackAnim.HOP);
            addToBot(new ApplyPowerAction(p, this, new TerrorPower(p, 1)));
            if (p.hasPower(TerrorPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
                BaseMod.subscribe(this);
            }
        });
        addMoveA(Intent.ATTACK, 8, 2, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                shout(0);
                ModHelper.addToBotAbstract(() -> AbstractDungeon.effectsQueue.add(new LaserBeamEffect(this.hb.cX, this.hb.cY)));
                attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                BaseMod.unsubscribe(this);
            }
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster instanceof SableclawWolftrooper || monster instanceof EclipseWolftrooper) {
                addToBot(new ApplyPowerAction(monster, this, new AngryPower(monster, 1)));
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
        } else if (AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).noneMatch(m -> m.hasPower(MoonRagePower.POWER_ID))
                && i < (ModHelper.specialAscension(type) ? 100 : 60)) {
            setMove(1);
        } else if (i < (lastMove((byte) 2) ? 66 : 33) - (ModHelper.specialAscension(type) ? 33 : 0)) {
            setMove(0);
        } else {
            setMove(2);
        }
    }

    int powerCount = 0;

    @Override
    public void receivePowersModified() {
        if (SubscriptionManager.checkSubscriber(this)) {
            int count = ModHelper.getPowerCount(p, TerrorPower.POWER_ID);
            if (powerCount != count) {
                powerCount = count;
                if (lastMove((byte) 2)) {
                    setMove(3);
                    createIntent();
                }
            }
        }
    }
}
