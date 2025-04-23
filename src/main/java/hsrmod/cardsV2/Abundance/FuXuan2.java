package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.NecrosisPower;

public class FuXuan2 extends BaseCard {
    public static final String ID = FuXuan2.class.getSimpleName();
    
    public FuXuan2() {
        super(ID);
        setBaseEnergyCost(135);
        tags.add(CustomEnums.ENERGY_COSTING);
        tags.add(CardTags.HEALING);
        isMultiDamage = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = (AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player) + block) * magicNumber / 100;
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new AddTemporaryHPAction(p, p, block));
        addToBot(new TriggerPowerAction(p.getPower(NecrosisPower.POWER_ID)));
        addToBot(new ApplyPowerAction(p, p, new NecrosisPower(p, 1)));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
}
