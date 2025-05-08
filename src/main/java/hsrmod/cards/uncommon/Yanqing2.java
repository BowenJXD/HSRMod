package hsrmod.cards.uncommon;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.FrozenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Consumer;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Yanqing2 extends BaseCard {
    public static final String ID = Yanqing2.class.getSimpleName();
    
    int cachedDamaged = 0;
    boolean canBeUsed = false;
    
    public Yanqing2() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && (canBeUsed || followedUp);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY,
                        new Consumer<ElementalDamageAction.CallbackInfo>() {
                            @Override
                            public void accept(ElementalDamageAction.CallbackInfo ci) {
                                if (AbstractDungeon.cardRandomRng.random(100) <= magicNumber)
                                    Yanqing2.this.addToBot(new ApplyPowerAction(ci.target, p, new FrozenPower(ci.target, 1), 1));
                            }
                        }
                )
        );
    }

    @Override
    public void atTurnStartPreDraw() {
        if (GameActionManager.damageReceivedThisTurn == cachedDamaged) {
            canBeUsed = true;
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        cachedDamaged = GameActionManager.damageReceivedThisTurn;
    }
}
