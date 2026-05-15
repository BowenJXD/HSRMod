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
        isMultiDamage = true;
        purgeOnUse = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0,1);
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        ModHelper.addToBotAbstract(() -> {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster.hasPower(AmphoreanHatredPower.POWER_ID)) {
                    int reduction = monster.getPower(AmphoreanHatredPower.POWER_ID).amount * 24 / 100;
                    addToTop(new ReducePowerAction(monster, p, AmphoreanHatredPower.POWER_ID, reduction));
                }
            }
            GeneralUtil.getRandomElements(p.exhaustPile.group, AbstractDungeon.cardRandomRng, magicNumber)
                    .forEach(c -> addToTop(new ExhaustToHandAction(c)));
        });
    }
}
