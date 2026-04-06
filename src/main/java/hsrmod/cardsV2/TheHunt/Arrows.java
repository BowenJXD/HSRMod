package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

public class Arrows extends BaseCard {
    public static final String ID = Arrows.class.getSimpleName();
    
    public Arrows() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new DrawCardAction(1));
        ModHelper.addToBotAbstract(() -> {
            p.hand.group.stream()
                    .filter(c  -> c.upgraded && c.costForTurn > 0 && !c.freeToPlayOnce)
                    .findFirst()
                    .ifPresent(c -> {
                        if (upgraded) c.freeToPlayOnce = true;
                        else c.setCostForTurn(c.costForTurn-1);
                    });
        });
    }
}
