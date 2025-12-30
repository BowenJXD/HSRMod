package hsrmod.monsters.TheBeyond;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PastConfinedAndCaged extends BaseMonster implements PreElementalDamageSubscriber {
    public static final String ID = PastConfinedAndCaged.class.getSimpleName();

    List<AbstractCard> cardsCache;
    CardBorderGlowManager.GlowInfo glowInfo;

    public PastConfinedAndCaged(float x, float y) {
        super(ID, -30, -15, 150, 384, x, y);

        addMoveA(Intent.ATTACK, 20, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
        });
        addMoveA(Intent.ATTACK_BUFF, 10, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.FAST);
            startSkill();
        });
        addMoveA(Intent.ATTACK, 40, mi -> {
                    if (hasPower(ChargingPower.POWER_ID)) {
                        attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AttackAnim.FAST);
                    }
                });
        addMove(Intent.STUN, mi->{});
        cardsCache = new ArrayList<>();
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return abstractCard.type == AbstractCard.CardType.ATTACK;
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
            healthBarUpdatedEvent();
        }
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ResonatePower.POWER_ID) && hasPower(BrokenPower.POWER_ID) && !lastMove((byte) 3)) {
            setMove(3);
        } else if (hasPower(ChargingPower.POWER_ID)) {
            setMove(2);
        } else if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
    
    void startSkill() {
        SubscriptionManager.subscribe(this);
        CardBorderGlowManager.removeGlowInfo(ID);
        CardBorderGlowManager.addGlowInfo(glowInfo);
        addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove()).setRemoveCallback(power ->{
            endSkill();
        })));
        cardsCache.clear();
    }
    
    void endSkill() {
        SubscriptionManager.unsubscribe(this);
        CardBorderGlowManager.removeGlowInfo(ID);
        cardsCache.clear();
        addToBot(new HandCheckAction());
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        endSkill();
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)
                && action.target == this
                && hasPower(ChargingPower.POWER_ID)
                && action.info.card != null
                && cardsCache.stream().noneMatch(c -> Objects.equals(c.cardID, action.info.card.cardID))) {
            cardsCache.add(action.info.card);
            damage.get(2).base -= specialAs ? 3 : 4;
            setMove(2);
            ModHelper.addToTopAbstract(this::createIntent);
        }
        return dmg;
    }
}
