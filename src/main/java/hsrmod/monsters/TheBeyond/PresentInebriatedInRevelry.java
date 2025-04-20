package hsrmod.monsters.TheBeyond;

import basemod.BaseMod;
import basemod.helpers.CardBorderGlowManager;
import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.SubscriptionManager;

public class PresentInebriatedInRevelry extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = PresentInebriatedInRevelry.class.getSimpleName();

    int windShearCount = 4;
    int shockCount = 2;
    int ultCount = 10;
    int ultCountCache = ultCount;
    int ultThreshold = 10;
    CardBorderGlowManager.GlowInfo glowInfo;

    public PresentInebriatedInRevelry(float x, float y) {
        super(ID, 150, 384, x, y);

        ultCount = ultCountCache = specialAs ? 12 : 10;
        ultThreshold = specialAs ? 8 : 10;

        addMove(Intent.DEBUFF, mi -> {
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, windShearCount)));
            addToBot(new ApplyPowerAction(p, this, new ShockPower(p, this, shockCount)));
        });
        addMove(Intent.DEBUFF, mi -> {
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, windShearCount / 2)));
            addToBot(new ApplyPowerAction(p, this, new ShockPower(p, this, shockCount / 2)));
            startSkill();
        });
        addMove(Intent.ATTACK_DEBUFF, 0, () -> ultCount, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                for (int i = 0; i < ultCount; i++) {
                    addToBot(new ApplyPowerAction(p, this, DoTPower.getRandomDoTPower(p, this, 1)));
                }
                addToBot(new TriggerDoTAction(p, this, 1, true));
            }
        });
        addMove(Intent.STUN, mi->{});
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.SKILL;
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.GOLD;
            }

            @Override
            public String glowID() {
                return ID;
            }
        };
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            addToBot(new ApplyPowerAction(this, this, new ResonatePower(this, 2, ResonatePower.ResonateType.PAST_PRESENT_AND_ETERNAL)));
            setHp(maxHealth * 3 / 2);
            healthBarUpdatedEvent();
        }
        if (moreDamageAs)
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, 1)));
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ResonatePower.POWER_ID) && hasPower(BrokenPower.POWER_ID) && !lastMove((byte) 3)) {
            setMove(3);
        } else if (hasPower(ChargingPower.POWER_ID)) {
            setMove(2);
        } else if (p.powers.stream().mapToInt(power -> power instanceof DoTPower ? power.amount : 0).sum() >= ultThreshold) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
    
    void startSkill() {
        BaseMod.subscribe(this);
        CardBorderGlowManager.removeGlowInfo(ID);
        CardBorderGlowManager.addGlowInfo(glowInfo);
        addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove()).setRemoveCallback(power -> {
            endSkill();
        })));
        ultCount = ultCountCache;
    }
    
    void endSkill() {
        BaseMod.unsubscribe(this);
        CardBorderGlowManager.removeGlowInfo(ID);
        addToBot(new HandCheckAction());
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        endSkill();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (SubscriptionManager.checkSubscriber(this)
                && hasPower(ChargingPower.POWER_ID)
                && abstractCard.type == AbstractCard.CardType.SKILL
                && ultCount > 0) {
            ultCount--;
            rollMove();
            createIntent();
        }
    }
}
