package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.breaks.BurnPower;

public class Jiaoqiu1 extends BaseCard {
    public static final String ID = Jiaoqiu1.class.getSimpleName();
    
    public Jiaoqiu1() {
        super(ID);
        selfRetain = true;
        energyCost = 100;
        cardsToPreview = new Jiaoqiu2();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        cardsToPreview.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard card = new Jiaoqiu2();
            if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card));
        }
        addToBot(new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage, damageTypeForTurn), 
                elementType, 2, AbstractGameAction.AttackEffect.FIRE, c ->{
            addToBot(new ApplyPowerAction(c, p, new VulnerablePower(c, 1, false), 1));
        })));
    }
}