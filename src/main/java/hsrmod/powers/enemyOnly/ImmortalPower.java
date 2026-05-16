package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.stream.Collectors;

public class ImmortalPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(ImmortalPower.class.getSimpleName());

    public ImmortalPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
        loadRegion("heartDef");
    }

    @Override
    public void onDeath() {
        super.onDeath();
        ModHelper.addToBotAbstract(() -> {
                    int num = AbstractDungeon.player.energy.energy - EnergyPanel.totalCount;
                    if (num > 0) {
                        addToTop(new GainEnergyAction(num));
                    }
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, EnergyPower.AMOUNT_LIMIT)));
                    addToTop(new ExhaustToHandAction(GeneralUtil.getRandomElement(
                            AbstractDungeon.player.exhaustPile.group.stream()
                                    .filter(c -> c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS).collect(Collectors.toList()), 
                            AbstractDungeon.cardRandomRng)));
                }
        );
    }
}
