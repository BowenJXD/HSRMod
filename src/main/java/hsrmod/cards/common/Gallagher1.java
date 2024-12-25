package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

public class Gallagher1 extends BaseCard {
    public static final String ID = Gallagher1.class.getSimpleName();

    public Gallagher1() {
        super(ID);
        energyCost = 110;
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.FIRE)
                .setCallback(
                        (ci) -> {
                            if (ci.didBreak || (ci.target.hasPower(BrokenPower.POWER_ID) && upgraded)) {
                                addToBot(new DrawCardAction(1));
                                addToBot(new ApplyPowerAction(ci.target, p, new BlockReturnPower(ci.target, 1), 1));
                            }
                        }
                )
        );
    }
}
