package hsrmod.monsters.TheBeyond;

import basemod.BaseMod;
import basemod.helpers.CardBorderGlowManager;
import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.AlienDreamPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class TomorrowInHarmoniousChords extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = TomorrowInHarmoniousChords.class.getSimpleName();

    int iniStrengthCount = 4;
    int skillStrengthCount = 3;
    CardBorderGlowManager.GlowInfo glowInfo;

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
            AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1)));
            });
            startSkill();
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
        });
        addMove(Intent.STUN, mi->{});
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.POWER;
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
        glowInfo.priority = -1;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        ModHelper.addToBotAbstract(this::startSkill);
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, iniStrengthCount)));
        addToBot(new RollMoveAction(this));
        ModHelper.addToBotAbstract(this::createIntent);
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            addToBot(new ApplyPowerAction(this, this, new ResonatePower(this, 2, ResonatePower.ResonateType.PAST_PRESENT_AND_ETERNAL)));
            setHp(maxHealth * 3 / 2);
            healthBarUpdatedEvent();
        }
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
    
    void startSkill() {
        BaseMod.subscribe(this);
        CardBorderGlowManager.removeGlowInfo(ID);
        CardBorderGlowManager.addGlowInfo(glowInfo);
        addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove()).setRemoveCallback(power -> {
            endSkill();
        })));
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
                && abstractCard.type == AbstractCard.CardType.POWER) {
            addToBot(new ReducePowerAction(this, this, StrengthPower.POWER_ID, 1));
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, 1)));
            ModHelper.addToBotAbstract(() -> {
                if (ModHelper.getPowerCount(this, StrengthPower.POWER_ID) <= 0) {
                    int tr = ModHelper.getPowerCount(this, ToughnessPower.POWER_ID);
                    addToTop(new ElementalDamageAction(this, new ElementalDamageInfo(this, 0, DamageInfo.DamageType.HP_LOSS, ElementType.None, tr), AbstractGameAction.AttackEffect.NONE));
                    addToBot(new RollMoveAction(this));
                    ModHelper.addToBotAbstract(this::createIntent);
                }
            });
        }
    }
}
