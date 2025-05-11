package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import hsrmod.actions.GridCardManipulator;
import hsrmod.actions.SimpleGridCardSelectBuilder;
import hsrmod.cards.BaseCard;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class MayRainbowsRemain extends BaseCard {
    public static final String ID = MayRainbowsRemain.class.getSimpleName();

    public MayRainbowsRemain() {
        super(ID);
        isInnate = true;
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new ChangeStanceAction(new CalmStance()));
        if (upgraded)
            addToBot(new SimpleGridCardSelectBuilder(c -> c.selfRetain)
                    .setCardGroup(p.drawPile, p.discardPile/*, p.exhaustPile*/)
                    .setAmount(magicNumber)
                    .setAnyNumber(true)
                    .setCanCancel(true)
                    .setMsg(GeneralUtil.tryFormat(RelicEventHelper.SELECT_TEXT, magicNumber))
                    .setManipulator(new GridCardManipulator() {
                        @Override
                        public boolean manipulate(AbstractCard card, int index, CardGroup group) {
                            moveToHand(card, group);
                            return false;
                        }
                    })
            );
        else
            ModHelper.findCards(card -> card.selfRetain, true)
                    .stream()
                    .filter(r -> r.group != AbstractDungeon.player.hand)
                    .limit(magicNumber)
                    .forEach(r -> {
                                addToTop(new MoveCardsAction(AbstractDungeon.player.hand, r.group, card -> card == r.card, 1));
                            }
                    );
    }
}
