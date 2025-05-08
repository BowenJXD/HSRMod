package hsrmod.cards.common;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

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
        addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, magicNumber), magicNumber));
        if (upgraded) {
            ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    for (AbstractMonster c : AbstractDungeon.getMonsters().monsters) {
                        if (!c.isDeadOrEscaped() && c.hasPower(BurnPower.POWER_ID)) {
                            Guinaifen1.this.addToBot(new ApplyPowerAction(c, p, new VulnerablePower(c, 1, false), 1));
                        }
                    }
                }
            });
        } else {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
        }
    }
}
