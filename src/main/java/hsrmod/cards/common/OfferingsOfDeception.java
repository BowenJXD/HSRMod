package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;

public class OfferingsOfDeception extends BaseCard {
    public static final String ID = OfferingsOfDeception.class.getSimpleName();

    public OfferingsOfDeception() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        AbstractDungeon.getMonsters().monsters.stream().filter(mo -> !DoTPower.hasAnyDoTPower(mo)).forEach(mo ->
                addToBot(new ApplyPowerAction(mo, p, DoTPower.getRandomDoTPower(mo, p, 1), 1)));
        if (upgraded)
            AbstractDungeon.getMonsters().monsters.stream().filter(DoTPower::hasAllDoTPower).forEach(mo ->
                    addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -1), -1)));
    }
}
