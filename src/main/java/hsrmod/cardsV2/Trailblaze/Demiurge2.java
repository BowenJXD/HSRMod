package hsrmod.cardsV2.Trailblaze;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.enemyOnly.AmphoreanHatredPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.stream.Collectors;

public class Demiurge2 extends BaseCard {
    public static final String ID = Demiurge2.class.getSimpleName();

    int costCache;
    
    public Demiurge2() {
        super(ID);
        isMultiDamage = true;
        purgeOnUse = true;
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0,1);
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        ModHelper.addToBotAbstract(() -> {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster.hasPower(AmphoreanHatredPower.POWER_ID)) {
                    AbstractPower power = monster.getPower(AmphoreanHatredPower.POWER_ID);
                    int reduction = power.amount * 24 / 100;
                    power.reducePower(reduction);
                    //addToTop(new ApplyPowerAction(monster, p, new AmphoreanHatredPower(monster, -reduction)));
                }
            }
            addToTop(new ExhaustToHandAction(GeneralUtil.getRandomElement(
                    AbstractDungeon.player.exhaustPile.group.stream()
                            .filter(c -> c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS).collect(Collectors.toList()),
                    AbstractDungeon.cardRandomRng)));
        });
        setCostForTurn(costCache);
    }
}
