package androidTestMod.cards.uncommon;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.uniqueDebuffs.WisemansFollyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
        long count = 0L;
        for (AbstractPower power : mo.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                count++;
            }
        }
        int debuffNum = (int) count;
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
