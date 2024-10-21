package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.utils.ModHelper;

public class Guinaifen1 extends BaseCard {
    public static final String ID = Guinaifen1.class.getSimpleName();

    public Guinaifen1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.FIRE
        ));
        addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, 1), 1));
        if (upgraded) {
            ModHelper.addToBotAbstract(() -> {
                AbstractDungeon.getMonsters().monsters.stream()
                        .filter(c -> !c.isDeadOrEscaped() && c.hasPower(BurnPower.POWER_ID))
                        .forEach(c -> {
                            addToBot(new ApplyPowerAction(c, p, new VulnerablePower(c, 1, false), 1));
                        });
            });
        } else {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
        }
    }
}
