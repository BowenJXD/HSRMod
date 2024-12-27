package hsrmod.cardsV2.Preservation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

public class Burst extends BaseCard {
    public static final String ID = Burst.class.getSimpleName();

    public Burst() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlock = m.currentBlock > 0;
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                ci -> {
                    if (hasBlock && ci.target.currentBlock <= 0) {
                        if (upgraded) {
                            addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], c -> c.isInnate, list -> {
                                list.forEach(c -> addToTop(new ReduceCostForTurnAction(c, c.costForTurn)));
                            }));
                        }
                        else {
                            p.hand.group.stream().filter(c -> c.isInnate).findAny().ifPresent(c -> {
                                addToBot(new ReduceCostForTurnAction(c, c.costForTurn));
                            });
                        }
                    }
                })
        );
    }
}
