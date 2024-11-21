package hsrmod.cardsV2.Curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class Imprison extends BaseCard {
    public static final String ID = Imprison.class.getSimpleName();
    
    AbstractGameAction actionCache;
    public int energyCache = 0;
    
    public Imprison() {
        super(ID, CardColor.COLORLESS);
        isEthereal = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        EnergyPower power = (EnergyPower) AbstractDungeon.player.getPower(EnergyPower.POWER_ID);
        if (power != null) {
            energyCache = power.amount;
            ModHelper.addToTopAbstract(() -> power.setLocked(true));
            actionCache = new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, -power.amount), -power.amount);
            addToTop(actionCache);
        }
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        EnergyPower power = (EnergyPower) AbstractDungeon.player.getPower(EnergyPower.POWER_ID);
        if (power != null) {
            ModHelper.addToTopAbstract(() -> power.setLocked(false));
        }
        if (actionCache != null && AbstractDungeon.actionManager.actions.contains(actionCache)) {
            AbstractDungeon.actionManager.actions.remove(actionCache);
            actionCache = null;
        }
        else {
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, energyCache), energyCache));
        }
    }
}
