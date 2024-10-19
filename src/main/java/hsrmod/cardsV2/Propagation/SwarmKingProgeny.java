package hsrmod.cardsV2.Propagation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.SporePower;

public class SwarmKingProgeny extends BaseCard {
    public static final String ID = SwarmKingProgeny.class.getSimpleName();
    
    public SwarmKingProgeny() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ElementalDamageAction action = new ElementalDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                ElementType.Wind, 1, AbstractGameAction.AttackEffect.POISON, q -> {
            addToBot(new ApplyPowerAction(q, p, new SporePower(q, 1), 1));
        });
        addToBot(new BouncingAction(m, energyOnUse, action));
        p.energy.use(EnergyPanel.totalCount);
    }
}
