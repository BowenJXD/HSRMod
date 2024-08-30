package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.only.WisemansFollyPower;

public class DrRatio1 extends BaseCard {
    public static final String ID = DrRatio1.class.getSimpleName();
    
    public DrRatio1() {
        super(ID);
        energyCost = 140;
        cardsToPreview = new DrRatio3();
        selfRetain = true;
    }

    @Override
    public void upgrade() {
        cardsToPreview.upgrade();
        super.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WisemansFollyPower(m, 1)));
        
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Imaginary,
                        2,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_HEAVY
                )
        );
        
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard card = new DrRatio3();
            if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card));
        }
    }
}
