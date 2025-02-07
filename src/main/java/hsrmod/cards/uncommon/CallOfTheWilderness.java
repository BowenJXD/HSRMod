package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class CallOfTheWilderness extends BaseCard {
    public static final String ID = CallOfTheWilderness.class.getSimpleName();
    
    public CallOfTheWilderness() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction((q) -> new VFXAction(new BossCrystalImpactEffect(q.hb.cX, q.hb.cY))));
        addToBot(new AllEnemyApplyPowerAction(p, magicNumber, (q) -> new WeakPower(q, magicNumber, false)));
        if (upgraded)
            addToBot(new AOEAction((q) -> {
                if (Math.abs(ModHelper.getPowerCount(q, StrengthPower.POWER_ID)) <= Math.abs(ModHelper.getPowerCount(p, StrengthPower.POWER_ID)))
                    return new ApplyPowerAction(q, p, new StrengthPower(q, -magicNumber), -magicNumber);
                return null;
            }));
    }
}
