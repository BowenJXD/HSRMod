package hsrmod.cardsV2.Curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Imprison extends BaseCard {
    public static final String ID = Imprison.class.getSimpleName();
    
    AbstractGameAction actionCache;
    public static int energyCache = 0;
    
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
        if (power != null && power.amount >= energyCache) {
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
        if (actionCache != null && AbstractDungeon.actionManager.actions.contains(actionCache)) {
            AbstractDungeon.actionManager.actions.remove(actionCache);
        }
        else if (AbstractDungeon.player.hand.group.stream().noneMatch(c -> c instanceof Imprison)) {
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, energyCache), energyCache));
            energyCache = 0;
        }
        if (power != null && AbstractDungeon.player.hand.group.stream().noneMatch(c -> c instanceof Imprison)) 
            ModHelper.addToTopAbstract(() -> power.setLocked(false));
        actionCache = null;
    }
}
