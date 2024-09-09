package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Gallagher1 extends BaseCard {
    public static final String ID = Gallagher1.class.getSimpleName();

    public Gallagher1() {
        super(ID);
        energyCost = 110;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        addToBot(
                new ElementalDamageAction(m, new DamageInfo(p, damage),
                ElementType.Fire, magicNumber,
                AbstractGameAction.AttackEffect.FIRE,
                (c) -> {
                    if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                            && (toughness > 0 || upgraded)) {
                        addToBot(new ApplyPowerAction(c, p, new BlockReturnPower(c, 1), 1));
                    }
                })
        );
    }
}
