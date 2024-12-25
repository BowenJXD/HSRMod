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
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.SporePower;

public class SwarmKingProgeny extends BaseCard {
    public static final String ID = SwarmKingProgeny.class.getSimpleName();

    public SwarmKingProgeny() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse <= 0) return;
        ElementalDamageAction action = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.POISON,
                ci -> {
                    addToBot(new ApplyPowerAction(ci.target, p, new SporePower(ci.target, 1), 1));
                }
        );
        addToBot(new BouncingAction(m, energyOnUse, action, this));
        p.energy.use(EnergyPanel.totalCount);
    }
}
