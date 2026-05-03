package hsrmod.cardsV2.Trailblaze;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.enemyOnly.AmphoreanHatredPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class Demiurge2 extends BaseCard {
    public static final String ID = Demiurge2.class.getSimpleName();

    public Demiurge2() {
        super(ID);
        exhaust = true;
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).setCallback(ci -> {
            if (ci.target != null) {
                AbstractPower hatred = ci.target.getPower(AmphoreanHatredPower.POWER_ID);
                if (hatred != null && hatred.amount > 0) {
                    int reduction = hatred.amount * 24 / 100;
                    addToTop(new ReducePowerAction(ci.target, p, AmphoreanHatredPower.POWER_ID, reduction));
                }
            }
        }));
        ModHelper.addToBotAbstract(() -> {
            GeneralUtil.getRandomElements(p.exhaustPile.group, AbstractDungeon.cardRandomRng, magicNumber)
                    .forEach(c -> addToTop(new ExhaustToHandAction(c)));
        });
    }
}
