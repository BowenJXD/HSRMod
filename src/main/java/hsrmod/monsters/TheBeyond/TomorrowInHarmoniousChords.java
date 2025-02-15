package hsrmod.monsters.TheBeyond;

import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import com.megacrit.cardcrawl.vfx.combat.MiracleEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.AlienDreamPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class TomorrowInHarmoniousChords extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = TomorrowInHarmoniousChords.class.getSimpleName();

    int iniStrengthCount = 4;
    int skillStrengthCount = 3;

    public TomorrowInHarmoniousChords(float x, float y) {
        super(ID, 150, 384, x, y);

        iniStrengthCount = specialAs ? 5 : 4;

        addMoveA(Intent.ATTACK_BUFF, 10, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, skillStrengthCount)));
        });
        addMoveA(Intent.ATTACK_BUFF, 20, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, skillStrengthCount - 1)));
        });
        addMove(Intent.BUFF, mi -> {
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1)));
            });
            BaseMod.subscribe(this);
        });
        addMove(Intent.STRONG_DEBUFF, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                addToBot(new VFXAction(new BossCrystalImpactEffect(p.hb.cX, p.hb.cY)));
                addToBot(new ApplyPowerAction(p, this, new AlienDreamPower(p, 1)));
                int strengthCount = ModHelper.getPowerCount(this, StrengthPower.POWER_ID);
                if (strengthCount > 0) {
                    addToBot(new VFXAction(new FastingEffect(hb.cX, hb.cY, Color.RED)));
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount)));
                }
            }
            BaseMod.unsubscribe(this);
        });
        addMove(Intent.STUN, mi->{});
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, iniStrengthCount)));
        addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        addToBot(new RollMoveAction(this));
        ModHelper.addToBotAbstract(this::createIntent);
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            addToBot(new ApplyPowerAction(this, this, new ResonatePower(this, 2, ResonatePower.ResonateType.PAST_PRESENT_AND_ETERNAL)));
            setHp(maxHealth * 3 / 2);
            healthBarUpdatedEvent();
        }
        BaseMod.subscribe(this);
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ResonatePower.POWER_ID) && hasPower(BrokenPower.POWER_ID) && !lastMove((byte) 4)) {
            setMove(4);
        } else if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
        } else if (ModHelper.getPowerCount(this, StrengthPower.POWER_ID) > 3 && !lastMove((byte) 3)) {
            setMove(2);
        } else if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (SubscriptionManager.checkSubscriber(this) 
                && hasPower(ChargingPower.POWER_ID) 
                && abstractCard.type == AbstractCard.CardType.POWER) {
            addToBot(new ReducePowerAction(this, this, StrengthPower.POWER_ID, 1));
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, 1)));
        }
    }
}
