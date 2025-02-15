package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class MoltenCorePower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(MoltenCorePower.class.getSimpleName());

    public MoltenCorePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (ModHelper.getPowerCount(owner, ToughnessPower.POWER_ID) > 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, SecondaryCombustionPower.POWER_ID));
            int tr = ToughnessPower.getStackLimit(owner);
            addToBot(new ElementalDamageAction(owner, new ElementalDamageInfo(owner, 0, DamageInfo.DamageType.HP_LOSS, ElementType.None, tr), AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        int energyCost = card.costForTurn;
        if (energyCost == -1) energyCost = card.energyOnUse;
        if (energyCost > 0) {
            remove(energyCost);
        }
    }
}
