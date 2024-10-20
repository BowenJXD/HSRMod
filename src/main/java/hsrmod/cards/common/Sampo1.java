package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.WindShearPower;

public class Sampo1 extends BaseCard {
    public static final String ID = Sampo1.class.getSimpleName();
    
    private int windShearStackNum = 1;
    
    public Sampo1() {
        super(ID);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(m, new DamageInfo(p, this.damage, 
                DamageInfo.DamageType.NORMAL), ElementType.Wind, 1, AbstractGameAction.AttackEffect.POISON,
                c -> {
                    if (!c.isDeadOrEscaped())
                        this.addToBot(new ApplyPowerAction(c, p, new WindShearPower(c, p, this.windShearStackNum), this.windShearStackNum));
                }
                );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction, this));
    }
}
