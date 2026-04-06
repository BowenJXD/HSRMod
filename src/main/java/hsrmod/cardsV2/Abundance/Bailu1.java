package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
    public void onLeaveHand() {
        super.onLeaveHand();
        if (!AbstractDungeon.player.hand.isEmpty() && upgraded) {
            ModHelper.addToBotAbstract(this::trigger);
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(this::trigger);
    }

    @Override
    public void onRetained() {
        super.onRetained();
        ModHelper.addToBotAbstract(this::trigger);
    }

    void trigger() {
        int handSize = AbstractDungeon.player.hand.size();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NecrosisPower(AbstractDungeon.player, 1)));
        addToTop(new TriggerPowerAction(AbstractDungeon.player.getPower(NecrosisPower.POWER_ID)));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DewDropPower(AbstractDungeon.player, handSize * magicNumber)));
        addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, handSize * magicNumber + block));
    }
}
