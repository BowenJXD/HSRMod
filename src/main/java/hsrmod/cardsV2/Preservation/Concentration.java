package hsrmod.cardsV2.Preservation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class Concentration extends BaseCard {
    public static final String ID = Concentration.class.getSimpleName();

    public Concentration() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m.currentBlock > 0) {
            ModHelper.findCards(card -> card.isInnate, true)
                    .stream()
                    .filter(r -> r.group != AbstractDungeon.player.hand)
                    .limit(magicNumber)
                    .forEach(r -> {
                                addToTop(new MoveCardsAction(AbstractDungeon.player.hand, r.group, card -> card == r.card, 1));
                            }
                    );
        }
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL)
        );
    }
}
