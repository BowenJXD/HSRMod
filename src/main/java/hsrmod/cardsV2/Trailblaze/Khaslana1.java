package hsrmod.cardsV2.Trailblaze;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThirdEyeEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueBuffs.ScourgePower;

public class Khaslana1 extends BaseCard {
    public static final String ID = Khaslana1.class.getSimpleName();
    
    public Khaslana1() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.TERRITORY);
        returnToHand = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        
        addToBot(new VFXAction(new ThirdEyeEffect(m.hb.cX, m.hb.cY)));
        
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyPowerAction(p, p, new ScourgePower(p, magicNumber)));
    }
}
