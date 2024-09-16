package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.uniqueBuffs.Trailblazer5Power;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Trailblazer6 extends BaseCard {
    public static final String ID = Trailblazer6.class.getSimpleName();

    public Trailblazer6() {
        super(ID);
        cardsToPreview = new Trailblazer5();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(Trailblazer5Power.POWER_ID))
            addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber * 2), magicNumber * 2));
        else {
            List<ModHelper.FindResult> fireFiles = ModHelper.findCards(c -> c instanceof Trailblazer5);
            for (ModHelper.FindResult result : fireFiles) {
                if (result.group == AbstractDungeon.player.hand) continue;
                addToBot(new MoveCardsAction(result.group, AbstractDungeon.player.hand, card -> card == result.card));
            }

            if (fireFiles.isEmpty()) {
                addToBot(new MakeTempCardInHandAction(new Trailblazer5()));
            }
        }
    }
}
