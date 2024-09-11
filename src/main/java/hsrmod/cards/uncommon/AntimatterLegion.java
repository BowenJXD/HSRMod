/*
package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class AntimatterLegion extends BaseCard {
    public static final String ID = AntimatterLegion.class.getSimpleName();
    
    int costCache = -1;
    
    public AntimatterLegion() {
        super(ID);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage),
                ElementType.Fire, magicNumber,
                AbstractGameAction.AttackEffect.FIRE,
                (c) -> {
                    if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) > 0) {
                        returnToHand = true;
                        setCostForTurn(costCache);
                    }
                }));
    }
}
*/
