package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueBuffs.LostNetherlandPower;
import hsrmod.powers.uniqueBuffs.NewbudPower;
import hsrmod.utils.ModHelper;

public class Pollux3 extends BaseCard {
    public static final String ID = Pollux3.class.getSimpleName();

    public Pollux3() {
        super(ID);
        tags.add(CustomEnums.TERRITORY);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = ModHelper.getPowerCount(AbstractDungeon.player, NewbudPower.POWER_ID);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        int dmg = baseDamage / magicNumber;
        ElementalDamageAction action = new ElementalDamageAction(m, new ElementalDamageInfo(p, dmg, DamageInfo.DamageType.NORMAL, elementType, tr / magicNumber), AbstractGameAction.AttackEffect.FIRE);
        addToBot(new BouncingAction(m, magicNumber, action));
        addToBot(new RemoveSpecificPowerAction(p, p, NewbudPower.POWER_ID));
        addToBot(new RemoveSpecificPowerAction(p, p, LostNetherlandPower.POWER_ID));
    }
}
