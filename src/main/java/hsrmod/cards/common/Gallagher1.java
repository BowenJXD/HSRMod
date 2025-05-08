package hsrmod.cards.common;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.CachedCondition;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;

import java.util.function.Consumer;

public class Gallagher1 extends BaseCard {
    public static final String ID = Gallagher1.class.getSimpleName();

    public Gallagher1() {
        super(ID);
        setBaseEnergyCost(110);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.FIRE)
                .setCallback(
                        new Consumer<ElementalDamageAction.CallbackInfo>() {
                            @Override
                            public void accept(ElementalDamageAction.CallbackInfo ci) {
                                if (ci.didBreak || (ci.target.hasPower(BrokenPower.POWER_ID) && upgraded)) {
                                    Gallagher1.this.addToBot(new DrawCardAction(1));
                                    Gallagher1.this.addToBot(new ApplyPowerAction(ci.target, p, new BlockReturnPower(ci.target, 1), 1));
                                }
                            }
                        }
                )
        );
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = upgraded && CachedCondition.check(CachedCondition.Key.ANY_BROKEN) ? GREEN_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
