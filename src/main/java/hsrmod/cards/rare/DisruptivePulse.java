package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class DisruptivePulse extends BaseCard {
    public static final String ID = DisruptivePulse.class.getSimpleName();
    
    public DisruptivePulse() {
        super(ID);
        energyCost = 40;
        exhaust = true;
        
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = ModHelper.getPowerCount(BreakEffectPower.POWER_ID);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage), ElementType.Fire, magicNumber, AbstractGameAction.AttackEffect.SLASH_HEAVY,
                (c) -> {
                    if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                            && (toughness > 0)) {
                        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, 240), 240));
                        int energyNum = Math.max(AbstractDungeon.player.energy.energy - EnergyPanel.getCurrentEnergy(), 0);
                        addToBot(new GainEnergyAction(energyNum));
                    }
                }));
    }
}
