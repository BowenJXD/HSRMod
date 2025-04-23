package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueDebuffs.WisemansFollyPower;

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
        debuffNum = (int) mo.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).count();
        baseDamage = cachedBaseDamage + debuffNum;
        super.calculateCardDamage(mo);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> m.hasPower(WisemansFollyPower.POWER_ID))) {
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }
    }
}
