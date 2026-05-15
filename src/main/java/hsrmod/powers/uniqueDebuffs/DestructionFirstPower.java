package hsrmod.powers.uniqueDebuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class DestructionFirstPower extends DebuffPower implements OnDrawPileShufflePower {
    public static final String POWER_ID = HSRMod.makePath(DestructionFirstPower.class.getSimpleName());
    
    int dmgMultiplier = 10;

    public DestructionFirstPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount * dmgMultiplier, amount);
    }

    @Override
    public void onShuffle() {
        flash();
        addToBot(new DamageAction(owner, new DamageInfo(owner, amount * dmgMultiplier)));
        ModHelper.addToBotAbstract(() -> {
            GeneralUtil.getRandomElements(AbstractDungeon.player.drawPile.group, AbstractDungeon.cardRandomRng, amount).forEach(c -> {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
            });
        });
    }
}