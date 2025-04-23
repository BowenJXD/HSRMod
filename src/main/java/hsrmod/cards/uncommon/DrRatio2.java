package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueDebuffs.WisemansFollyPower;

public class DrRatio2 extends BaseCard {
    public static final String ID = DrRatio2.class.getSimpleName();
    
    int cachedBaseDamage = 0;
    
    public DrRatio2() {
        super(ID);
        cardsToPreview = new DrRatio3();
        cachedBaseDamage = baseDamage;
    }

    @Override
    public void upgrade() {
        cardsToPreview.upgrade();
        super.upgrade();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int debuffNum = (int) mo.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).count();
        baseDamage = cachedBaseDamage + debuffNum;
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WisemansFollyPower(m, 1)));
        
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this, damage),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY
                )
        );

        AbstractCard card = new DrRatio3();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInHandAction(card));
    }
}
