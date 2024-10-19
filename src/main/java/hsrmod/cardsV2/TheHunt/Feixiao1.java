package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class Feixiao1 extends BaseCard {
    public static final String ID = Feixiao1.class.getSimpleName();

    public Feixiao1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), elementType, 1));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        ModHelper.addToBotAbstract(() -> {
            if (c.type == CardType.ATTACK
                    && !Objects.equals(c.cardID, cardID)
                    && (AbstractDungeon.player.drawPile.isEmpty() || !Objects.equals(AbstractDungeon.player.drawPile.getTopCard().cardID, cardID))) {
                flash();
                addToTop(new MakeTempCardInDrawPileAction(new Feixiao1(), 1, false, true));
            }
        });
    }
}
