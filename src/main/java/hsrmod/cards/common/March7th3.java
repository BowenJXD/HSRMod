package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.breaks.FrozenPower;

public class March7th3 extends BaseCard {
    public static final String ID = March7th3.class.getSimpleName();
    
    public March7th3() {
        super(ID);
        energyCost = 120;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction((q) -> 
            new ElementalDamageAction(q, new DamageInfo(p, damage, damageTypeForTurn), elementType, 2, 
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, (c) -> {
                if (AbstractDungeon.cardRng.random(99) < 50)
                    addToTop(new ApplyPowerAction(c, p, new FrozenPower(c, magicNumber), magicNumber));
            })
        ));
    }
}
