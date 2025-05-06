package androidTestMod.cards.common;

import androidTestMod.actions.BouncingAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.breaks.WindShearPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Consumer;

public class Sampo1 extends BaseCard {
    public static final String ID = Sampo1.class.getSimpleName();

    private int windShearStackNum = 1;

    public Sampo1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.POISON,
                new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (!ci.target.isDeadOrEscaped())
                            Sampo1.this.addToBot(new ApplyPowerAction(
                                    ci.target,
                                    p,
                                    new WindShearPower(ci.target, p, Sampo1.this.windShearStackNum),
                                    Sampo1.this.windShearStackNum
                            ));
                    }
                }
        );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction, this));
    }
}
