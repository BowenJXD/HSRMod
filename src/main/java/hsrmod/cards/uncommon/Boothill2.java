package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.ToughnessPower;
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
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        addToBot(new ElementalDamageAction(m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                (c) -> {
            int newToughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
                    if (newToughness > 0 && toughness >= newToughness) {
                        returnToHand = true;
                        setCostForTurn(costCache);
                    }
                }));
    }
}
