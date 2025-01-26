package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.breaks.FrozenPower;

public class March7th3 extends BaseCard {
    public static final String ID = March7th3.class.getSimpleName();

    public March7th3() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(ci -> {
                    if (AbstractDungeon.cardRng.random(99) < 50)
                        addToBot(new ApplyPowerAction(ci.target, p, new FrozenPower(ci.target, magicNumber), magicNumber));
                })
        );
    }
}
