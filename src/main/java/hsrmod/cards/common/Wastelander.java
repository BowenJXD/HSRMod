package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Wastelander extends BaseCard {
    public static final String ID = Wastelander.class.getSimpleName();
    
    public Wastelander() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage), ElementType.Lightning, magicNumber, AbstractGameAction.AttackEffect.LIGHTNING,
                (c) -> {
                    if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                            && (toughness > 0 || upgraded)) {
                        int drawNum = c.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).mapToInt(power -> power.amount).sum();
                        addToBot(new DrawCardAction(drawNum));
                    }
                }));
    }
}
