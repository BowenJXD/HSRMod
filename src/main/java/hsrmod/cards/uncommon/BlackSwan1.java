package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueDebuffs.EpiphanyPower;

public class BlackSwan1 extends BaseCard {
    public static final String ID = BlackSwan1.class.getSimpleName();
    
    public BlackSwan1() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.target = CardTarget.ALL_ENEMY;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage, damageTypeForTurn),
                    elementType, 2, AbstractGameAction.AttackEffect.POISON, c -> {
                addToBot(new ApplyPowerAction(c, p, new EpiphanyPower(c, 1), 1));
            })));
        }
        else {
            addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    elementType, 2, AbstractGameAction.AttackEffect.POISON, c -> {
                addToBot(new ApplyPowerAction(c, p, new EpiphanyPower(c, 1), 1));
            }));
        }
    }
}
