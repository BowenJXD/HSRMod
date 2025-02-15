package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.InversionBeamEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;

public class SilverWolf1 extends BaseCard {
    public static final String ID = SilverWolf1.class.getSimpleName();
    
    public SilverWolf1() {
        super(ID);
        setBaseEnergyCost(110);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (Settings.FAST_MODE)
            shout(1);
        else
            shout(0);
        if (m != null)
            addToBot(new VFXAction(new InversionBeamEffect(m.hb.cX)));
        
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        ));
        for (int i = 0; i < magicNumber; i++) {
            switch (AbstractDungeon.cardRandomRng.random(upgraded ? 2 : 1)) {
                case 0:
                    addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
                    break;
                case 1:
                    addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
                    break;
                case 2:
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
                    break;
            }
        }
    }
}
