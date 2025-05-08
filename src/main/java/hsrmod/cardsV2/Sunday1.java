package hsrmod.cardsV2;

import hsrmod.actions.CleanAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Sunday1 extends BaseCard {
    public static final String ID = Sunday1.class.getSimpleName();
    
    public Sunday1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new SpotlightEffect()));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(CustomEnums.FOLLOW_UP);
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> list) {
                if (!list.isEmpty()) {
                    for (AbstractCard c : list) {
                        Sunday1.this.addToBot(new FollowUpAction(c, m, true));
                    }
                }
            }
        }));
        if (upgraded) addToBot(new CleanAction(p, 1, true));
    }
}
