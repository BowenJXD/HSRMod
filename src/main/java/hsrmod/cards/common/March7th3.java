package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.breaks.FrozenPower;

import java.util.function.Consumer;

public class March7th3 extends BaseCard {
    public static final String ID = March7th3.class.getSimpleName();

    public March7th3() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new BlizzardEffect(7, false)));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (AbstractDungeon.cardRng.random(99) < 50)
                            March7th3.this.addToBot(new ApplyPowerAction(ci.target, p, new FrozenPower(ci.target, magicNumber), magicNumber));
                    }
                })
        );
    }
}
