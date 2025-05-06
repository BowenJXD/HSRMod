package androidTestMod.cards.common;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.function.Consumer;

public class Pela1 extends BaseCard {
    public static final String ID = Pela1.class.getSimpleName();

    public Pela1() {
        super(ID);
        setBaseEnergyCost(100);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        Pela1.this.addToBot(new ApplyPowerAction(ci.target, p, new VulnerablePower(ci.target, magicNumber, false), magicNumber));
                    }
                })
        );
    }
}
