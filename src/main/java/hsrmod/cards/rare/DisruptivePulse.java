package hsrmod.cards.rare;

import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.function.Consumer;

public class DisruptivePulse extends BaseCard {
    public static final String ID = DisruptivePulse.class.getSimpleName();

    public DisruptivePulse() {
        super(ID);
        setBaseEnergyCost(80);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.FIRE,
                new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (ci.didBreak) {
                            DisruptivePulse.this.addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, EnergyPower.AMOUNT_LIMIT), EnergyPower.AMOUNT_LIMIT));
                            int energyNum = AbstractDungeon.player.energy.energy - EnergyPanel.getCurrentEnergy();
                            if (energyNum > 0) DisruptivePulse.this.addToBot(new GainEnergyAction(energyNum));
                            DisruptivePulse.this.addToBot(new BreakDamageAction(ci.target, new DamageInfo(p, tr)));
                        }
                    }
                })
        );
    }
}
