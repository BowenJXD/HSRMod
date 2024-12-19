package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;

import java.util.Comparator;

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
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        int useCount = 0;
        if (p.hand.group.stream().anyMatch(c -> c.hasTag(CustomEnums.ENERGY_COSTING) && c != this)) useCount++;
        if (upgraded && p.hand.group.stream().anyMatch(c -> (c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != this)) useCount++;
        
        AbstractMonster monsterWithMostHealth = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped() && mo.currentHealth > 0 && !mo.halfDead)
                .max(Comparator.comparingInt(m2 -> m2.currentHealth))
                .orElse(null);
        if (monsterWithMostHealth != null) {
            for (int i = 0; i < useCount; i++) {
                addToBot(new ElementalDamageAction(monsterWithMostHealth, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            }
        }
    }
}
