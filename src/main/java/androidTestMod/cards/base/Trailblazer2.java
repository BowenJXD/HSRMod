package androidTestMod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;

public class Trailblazer2 extends BaseCard {
    public static final String ID = Trailblazer2.class.getSimpleName();

    public Trailblazer2() {
        super(ID);
        this.isMultiDamage = true;
        // this.tags.add(CardTags.STRIKE);
        // this.tags.add(CardTags.STARTER_STRIKE);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

}