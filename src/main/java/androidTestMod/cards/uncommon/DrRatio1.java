package androidTestMod.cards.uncommon;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.uniqueDebuffs.WisemansFollyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DrRatio1 extends BaseCard {
    public static final String ID = DrRatio1.class.getSimpleName();
    
    public DrRatio1() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new DrRatio3();
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
                        new ElementalDamageInfo(this),
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
