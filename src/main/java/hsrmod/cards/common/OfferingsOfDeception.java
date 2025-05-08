package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class OfferingsOfDeception extends BaseCard {
    public static final String ID = OfferingsOfDeception.class.getSimpleName();

    public OfferingsOfDeception() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!DoTPower.hasAnyDoTPower(monster)) {
                OfferingsOfDeception.this.addToBot(new ApplyPowerAction(monster, p, DoTPower.getRandomDoTPower(monster, p, 1), 1));
            }
        }
        if (upgraded) {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (DoTPower.hasAllDoTPower(mo)) {
                    OfferingsOfDeception.this.addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -1), -1));
                }
            }
        }
    }
}
