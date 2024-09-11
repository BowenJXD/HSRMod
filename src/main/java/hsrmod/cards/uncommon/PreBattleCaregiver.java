package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class PreBattleCaregiver extends BaseCard {
    public static final String ID = PreBattleCaregiver.class.getSimpleName();
    
    public PreBattleCaregiver() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(m, new DamageInfo(p, damage),
                        ElementType.Physical, magicNumber,
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
        int toughness = ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        ModHelper.addToBotAbstract(() -> {
            if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) <= 0
                    && (toughness > 0 || upgraded)){
                int val = ToughnessPower.getStackLimit(m);
                addToBot(new GainBlockAction(p, val));
            }
        });
    }
}
