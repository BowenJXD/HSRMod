package androidTestMod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import androidTestMod.actions.BreakDamageAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.misc.ToughnessPower;
import androidTestMod.utils.ModHelper;

public class CourtOfHomogeneity extends BaseCard {
    public static final String ID = CourtOfHomogeneity.class.getSimpleName();
    
    public CourtOfHomogeneity() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, p.hand.size(), true));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.FIRE));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                int dmg = ModHelper.getPowerCount(p, ToughnessPower.POWER_ID) - ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
                if (dmg > 0) {
                    CourtOfHomogeneity.this.addToBot(new BreakDamageAction(m, new DamageInfo(p, dmg)));
                }
            }
        });
        addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
