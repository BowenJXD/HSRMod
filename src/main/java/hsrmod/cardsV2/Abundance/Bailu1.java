package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DewDropPower;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.utils.ModHelper;

public class Bailu1 extends BaseCard {
    public static final String ID = Bailu1.class.getSimpleName();

    public Bailu1() {
        super(ID);
        selfRetain = true;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void onMove(CardGroup group, boolean in) {
        super.onMove(group, in);
        if  (in && group == AbstractDungeon.player.discardPile) {
            ModHelper.addToBotAbstract(this::trigger);
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }

    void trigger() {
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NecrosisPower(AbstractDungeon.player, 1)));
        addToTop(new TriggerPowerAction(AbstractDungeon.player.getPower(NecrosisPower.POWER_ID)));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DewDropPower(AbstractDungeon.player, block)));
        addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, block));
        addToTop(new DrawCardAction(AbstractDungeon.player, magicNumber));
    }
}
