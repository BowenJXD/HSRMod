package androidTestMod.cards.uncommon;

import androidTestMod.actions.AOEAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.breaks.BleedingPower;
import androidTestMod.powers.breaks.BurnPower;
import androidTestMod.powers.breaks.ShockPower;
import androidTestMod.powers.breaks.WindShearPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;

import java.util.function.Function;

public class BlackSwan2 extends BaseCard {
    public static final String ID = BlackSwan2.class.getSimpleName();
    
    private int bleedStackNum = 1;
    private int burnStackNum = 1;
    private int shockStackNum = 1;
    private int windShearStackNum = 2;
    
    public BlackSwan2() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
            @Override
            public AbstractGameAction apply(AbstractMonster q) {
                return new VFXAction(new BossCrystalImpactEffect(q.hb.cX, q.hb.cY));
            }
        }));
        addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, p, bleedStackNum), bleedStackNum));
        addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, burnStackNum), burnStackNum));
        addToBot(new ApplyPowerAction(m, p, new ShockPower(m, p, shockStackNum), shockStackNum));
        addToBot(new ApplyPowerAction(m, p, new WindShearPower(m, p, magicNumber), magicNumber));
    }
    
    
}
