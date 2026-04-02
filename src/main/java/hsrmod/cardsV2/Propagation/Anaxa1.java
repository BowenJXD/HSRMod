package hsrmod.cardsV2.Propagation;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.SporePower;
import hsrmod.utils.GAMManager;
import hsrmod.utils.ModHelper;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anaxa1 extends BaseCard {
    public static final String ID = Anaxa1.class.getSimpleName();
    
    public Anaxa1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse + (p.hasRelic("Chemical X") ? 2 : 0);
        if (x <= 0) return;

        GAMManager.addParallelAction(ID, action -> {
            if (action instanceof ApplyPowerAction) {
                ApplyPowerAction ap = (ApplyPowerAction) action;
                AbstractPower power = ReflectionHacks.getPrivate(ap, ApplyPowerAction.class, "powerToApply");
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    GAMManager.stopCurrentAction();
                }
            } else if (action instanceof ReducePowerAction) {
                ReducePowerAction rp = (ReducePowerAction) action;
                AbstractPower power = ReflectionHacks.getPrivate(rp, ReducePowerAction.class, "powerInstance");
                if (power == null) {
                    String powerID = ReflectionHacks.getPrivate(rp, ReducePowerAction.class, "powerID");
                    if (powerID != null && rp.target != null) {
                        power = rp.target.getPower(powerID);
                    }
                }
                if (power != null && power.type == AbstractPower.PowerType.DEBUFF) {
                    GAMManager.stopCurrentAction();
                }
            } else if (action instanceof RemoveSpecificPowerAction) {
                RemoveSpecificPowerAction rp = (RemoveSpecificPowerAction) action;
                AbstractPower power = ReflectionHacks.getPrivate(rp, RemoveSpecificPowerAction.class, "powerInstance");
                if (power == null) {
                    String powerID = ReflectionHacks.getPrivate(rp, RemoveSpecificPowerAction.class, "powerID");
                    if (powerID != null && rp.target != null) {
                        power = rp.target.getPower(powerID);
                    }
                }
                if (power != null && power.type == AbstractPower.PowerType.DEBUFF) {
                    GAMManager.stopCurrentAction();
                }
            } 
            return true;
        });
        
        ElementalDamageAction action = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        );
        addToBot(new BouncingAction(m, x, action, this));
        
        ModHelper.addToBotAbstract(() -> GAMManager.removeParallelAction(ID));
        
        if (!freeToPlayOnce)
            addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
