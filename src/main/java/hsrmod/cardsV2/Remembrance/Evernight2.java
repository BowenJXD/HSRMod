package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.ObsessionPower;
import hsrmod.powers.uniqueDebuffs.LoseFocusPower;

public class Evernight2 extends BaseCard {
    public static final String ID = Evernight2.class.getSimpleName();

    public Evernight2() {
        super(ID);
        isMultiDamage = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new LoseHPAction(p, p, 1));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(p, p, new ObsessionPower(p, magicNumber)));
    }
}
