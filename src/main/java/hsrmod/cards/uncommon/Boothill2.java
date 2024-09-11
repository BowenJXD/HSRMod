package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueDebuffs.ThanatoplumRebloomPower;
import hsrmod.utils.ModHelper;

public class Boothill2 extends BaseCard {
    public static final String ID = Boothill2.class.getSimpleName();

    int costCache = -1;
    
    public Boothill2() {
        super(ID);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage),
                elementType, magicNumber,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                (c) -> {
                    if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) > 0) {
                        returnToHand = true;
                        setCostForTurn(costCache);
                    }
                }));
    }
}
