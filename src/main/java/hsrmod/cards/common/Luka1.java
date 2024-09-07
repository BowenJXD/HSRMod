package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.uniqueBuffs.TopazNumbyPower;

public class Luka1 extends BaseCard {
    public static final String ID = Luka1.class.getSimpleName();
    
    public Luka1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ElementalDamageAction(m, new DamageInfo(p, this.damage,
                    DamageInfo.DamageType.NORMAL), ElementType.Fire, 1, AbstractGameAction.AttackEffect.FIRE));
            addToBot(new TriggerDoTAction(m, 1, BleedingPower.POWER_ID));
        }
        addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, p, 1), 1));
    }
}
