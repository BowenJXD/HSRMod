package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueDebuffs.WisemansFollyPower;

import java.util.function.Predicate;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class DrRatio3 extends BaseCard {
    public static final String ID = DrRatio3.class.getSimpleName();
    
    int cachedBaseDamage = 0;
    public int debuffNum = 0;
    
    public DrRatio3() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
        cachedBaseDamage = baseDamage;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this, damage),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY
                )
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && followedUp;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        long count = 0L;
        for (AbstractPower power : mo.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                count++;
            }
        }
        debuffNum = (int) count;
        baseDamage = cachedBaseDamage + debuffNum;
        super.calculateCardDamage(mo);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        boolean b = true;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.hasPower(WisemansFollyPower.POWER_ID)) {
                b = false;
                break;
            }
        }
        if (b) {
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }
    }
}
