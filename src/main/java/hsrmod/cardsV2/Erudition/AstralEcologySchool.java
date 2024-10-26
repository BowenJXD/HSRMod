package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class AstralEcologySchool extends BaseCard {
    public static final String ID = AstralEcologySchool.class.getSimpleName();
    
    public AstralEcologySchool() {
        super(ID);
        energyCost = 100;
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int useCount = 1;
        if (p.hand.group.stream().anyMatch(c -> c.hasTag(CustomEnums.ENERGY_COSTING) && c != this)) useCount++;
        if (upgraded && p.hand.group.stream().anyMatch(c -> (c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != this)) useCount++;
        
        for (int i = 0; i < useCount; i++) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
    }
}
