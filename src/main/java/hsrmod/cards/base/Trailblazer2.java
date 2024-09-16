package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

public class Trailblazer2 extends BaseCard {
    public static final String ID = Trailblazer2.class.getSimpleName();

    public Trailblazer2() {
        super(ID);
        this.isMultiDamage = true;
        // this.tags.add(CardTags.STRIKE);
        // this.tags.add(CardTags.STARTER_STRIKE);
        energyCost = 120;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!checkEnergy()) return;
        addToBot(
                new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage), ElementType.Physical, toughnessReduction, 
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
        );
    }

}