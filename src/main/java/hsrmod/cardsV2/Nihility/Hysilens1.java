package hsrmod.cardsV2.Nihility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.uniqueDebuffs.SoulstruckPower;

public class Hysilens1 extends BaseCard {
    public static final String ID = Hysilens1.class.getSimpleName();

    public Hysilens1() {
        super(ID);
        isEthereal = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (upgraded) {
            addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, p, magicNumber), magicNumber));
        }
        addToBot(new ApplyPowerAction(m, p, new SoulstruckPower(m, 1), 1));
    }
}
