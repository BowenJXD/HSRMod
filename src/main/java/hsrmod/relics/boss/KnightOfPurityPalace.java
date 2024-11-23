package hsrmod.relics.boss;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import hsrmod.cards.BaseCard;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class KnightOfPurityPalace extends BaseRelic {
    public static final String ID = KnightOfPurityPalace.class.getSimpleName();

    public KnightOfPurityPalace() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStart();
        AbstractDungeon.player.drawPile.group.stream().filter((c) -> c.hasTag(BaseCard.CardTags.STARTER_DEFEND))
                .forEach((c) -> {
                    c.isInnate = true;
                    c.baseBlock += 2;
                    c.applyPowers();
                });
    }

    @Override
    public void onShuffle() {
        super.onShuffle();
        addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, (c) -> c.isInnate, BaseMod.MAX_HAND_SIZE));
        addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, (c) -> c.isInnate, BaseMod.MAX_HAND_SIZE));
        // addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.exhaustPile, (c) -> c.isInnate, 99));
    }
}
