package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DewDropPower;
import hsrmod.utils.ModHelper;

public class TimeWaitsForNoOne extends BaseCard {
    public static final String ID = TimeWaitsForNoOne.class.getSimpleName();
    
    public TimeWaitsForNoOne() {
        super(ID);
        selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (inHand && c.tags.contains(CardTags.HEALING)) {
            addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DewDropPower(AbstractDungeon.player, magicNumber)));
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
        if (!upgraded) return;
        int dewDrop = ModHelper.getPowerCount(AbstractDungeon.player, DewDropPower.POWER_ID);
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DewDropPower(AbstractDungeon.player, dewDrop)));
        int tempHp = TempHPField.tempHp.get(AbstractDungeon.player);
        addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, tempHp));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
    }

    @Override
    protected boolean checkSpawnable() {
        if (AbstractDungeon.player.masterDeck.findCardById(cardID) != null) return false;
        return super.checkSpawnable();
    }
}
