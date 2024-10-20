package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;

public class Jiaoqiu1 extends BaseCard {
    public static final String ID = Jiaoqiu1.class.getSimpleName();
    
    public Jiaoqiu1() {
        super(ID);
        energyCost = 100;
        isMultiDamage = true;
        cardsToPreview = new Jiaoqiu2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard card = new Jiaoqiu2();
            // if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card));
        }
        addToBot(new ElementalDamageAllAction(p, multiDamage, damageTypeForTurn, elementType, 2, AbstractGameAction.AttackEffect.FIRE).setCallback(c -> {
            addToBot(new ApplyPowerAction(c, p, new VulnerablePower(c, 1, false), 1));
        }));
    }
}
